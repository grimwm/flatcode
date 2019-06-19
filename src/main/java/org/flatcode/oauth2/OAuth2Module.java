package org.flatcode.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStore;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.common.cache.LoadingCache;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.PrivateModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import org.flatcode.oauth2.cache.CredentialCacheBuilder;
import org.flatcode.oauth2.model.OAuth2ClientCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.time.Duration;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.flatcode.common.MorePreconditions.checkNotNullOrEmpty;

/**
 * Providers for OAuth2
 *
 * @author wgrim
 * @since 2018-11-24
 */
public class OAuth2Module extends AbstractModule {

  private static final Logger log = LoggerFactory.getLogger(OAuth2Module.class);

  private static final String OAUTH2_CREDENTIALS_DIRECTORY = "oauth2.credentials_directory";
  private static final String OAUTH2_MAX_CACHE_SIZE = "oauth2.cache.max.items";

  private static DataStoreFactory dataStoreFactory;

  private Class<? extends Annotation> annotation;
  private URL credentialsUrl;
  private Duration accessTokenDuration;
  private GenericUrl requestAuthUrl;
  private GenericUrl getTokenURL;

  private OAuth2Module(Builder builder) {
    this.annotation = checkNotNull(builder.annotation);
    this.credentialsUrl= checkNotNull(builder.credentialsUrl);
    this.accessTokenDuration = checkNotNull(builder.accessTokenDuration);
    this.requestAuthUrl = new GenericUrl(checkNotNullOrEmpty(builder.requestAuthUrl));
    this.getTokenURL = new GenericUrl(checkNotNullOrEmpty(builder.getTokenURL));
  }

  @Override
  protected void configure() {
    install(new Oauth2PrivateModule(
        annotation, credentialsUrl, accessTokenDuration, requestAuthUrl, getTokenURL,
        Integer.valueOf(System.getProperty(OAUTH2_MAX_CACHE_SIZE, "1000"))));
  }

  private static synchronized DataStoreFactory getDataStoreFactory() throws IOException {
    if (dataStoreFactory != null) {
      return dataStoreFactory;
    }

    File tmpDir = new File(System.getProperty(OAUTH2_CREDENTIALS_DIRECTORY, System.getProperty("java.io.tmpdir")));
    log.debug("Opening {} at {}", OAUTH2_CREDENTIALS_DIRECTORY, tmpDir.getAbsolutePath());
    dataStoreFactory = new FileDataStoreFactory(tmpDir);
    return dataStoreFactory;
  }

  public static Builder builder(Class<? extends Annotation> annotation) {
    return new Builder(annotation);
  }

  private static class Oauth2PrivateModule extends PrivateModule {

    private static final TypeLiteral<LoadingCache<String, Credential>> LOADING_CACHE_TYPE_LITERAL = new TypeLiteral<LoadingCache<String, Credential>>(){};

    private Class<? extends Annotation> annotation;
    private URL credentialsUrl;
    private Duration accessTokenDuration;
    private GenericUrl requestAuthUrl;
    private GenericUrl getTokenUrl;
    private Integer maxCacheSize;

    Oauth2PrivateModule(Class<? extends Annotation> annotation, URL credentialsUrl,
        Duration accessTokenDuration, GenericUrl requestAuthUrl, GenericUrl getTokenUrl, Integer maxCacheSize) {
      this.annotation = annotation;
      this.credentialsUrl = credentialsUrl;
      this.accessTokenDuration = accessTokenDuration;
      this.requestAuthUrl = requestAuthUrl;
      this.getTokenUrl = getTokenUrl;
      this.maxCacheSize = maxCacheSize;
    }

    @Override
    protected void configure() {
      bind(Key.get(AuthorizationCodeFlow.class, annotation)).to(AuthorizationCodeFlow.class);
      expose(Key.get(AuthorizationCodeFlow.class, annotation));
      bind(Key.get(OAuth2ClientCredentials.class, annotation)).to(OAuth2ClientCredentials.class);
      expose(Key.get(OAuth2ClientCredentials.class, annotation));
      bind(Key.get(LOADING_CACHE_TYPE_LITERAL, annotation)).to(LOADING_CACHE_TYPE_LITERAL);
      expose(Key.get(LOADING_CACHE_TYPE_LITERAL, annotation));
    }

    @Provides @Singleton
    public OAuth2ClientCredentials provideCredentials(ObjectMapper mapper) throws Exception {
      OAuth2ClientCredentials credentials = mapper.readValue(credentialsUrl, OAuth2ClientCredentials.class);
      log.debug("Credentials stored at {}: {}", credentialsUrl, credentials);
      return credentials;
    }

    @Provides @Singleton
    public AuthorizationCodeFlow provideAuthorizationCodeFlow(OAuth2ClientCredentials credentials,
        DataStore<StoredCredential> credentialDataStore) {
      return new AuthorizationCodeFlow.Builder(
          BearerToken.authorizationHeaderAccessMethod(),
          new NetHttpTransport(),
          JacksonFactory.getDefaultInstance(),
          getTokenUrl,
          new ClientParametersAuthentication(
              credentials.getClientId(),
              credentials.getClientSecret()),
          credentials.getClientId(),
          requestAuthUrl.build())
          .setCredentialDataStore(credentialDataStore)
          .build();
    }

    @Provides @Singleton
    public DataStore<StoredCredential> provideCredentialsDataStore() throws IOException {
      return getDataStoreFactory().getDataStore(annotation.getSimpleName().toLowerCase());
    }

    @Provides @Singleton
    public LoadingCache<String, Credential> provideCredentialCache(
        AuthorizationCodeFlow authorizationCodeFlow) {
      return new CredentialCacheBuilder()
          .maxCacheSize(maxCacheSize)
          .authorizationCodeFlow(authorizationCodeFlow)
          .cacheEntryTimeout(accessTokenDuration)
          .build();
    }
  }

  public static class Builder {

    private Class<? extends Annotation> annotation;
    private URL credentialsUrl;
    private Duration accessTokenDuration;
    private String requestAuthUrl;
    private String getTokenURL;

    private Builder(Class<? extends Annotation> annotation) {
      this.annotation = annotation;
    }

    public Builder credentialsUrl(URL url) {
      this.credentialsUrl = url;
      return this;
    }

    public Builder accessTokenDuration(Duration duration) {
      this.accessTokenDuration = duration;
      return this;
    }

    public Builder requestAuthUrl(String url) {
      this.requestAuthUrl = url;
      return this;
    }

    public Builder getTokenUrl(String url) {
      this.getTokenURL = url;
      return this;
    }

    public OAuth2Module build() {
      return new OAuth2Module(this);
    }
  }
}
