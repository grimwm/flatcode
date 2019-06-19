package org.flatcode.oauth2.cache;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.annotation.Nonnull;
import java.time.Duration;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

/**
 * A builder for generating {@link LoadingCache}s storing {@link Credential}s keyed by {@link String}.
 *
 * @author wgrim
 * @since 2018-12-04
 */
public class CredentialCacheBuilder {

  private AuthorizationCodeFlow authorizationCodeFlow;
  private long maxCacheSize;
  private Duration cacheEntryTimeout;

  public CredentialCacheBuilder authorizationCodeFlow(AuthorizationCodeFlow authorizationCodeFlow) {
    this.authorizationCodeFlow = authorizationCodeFlow;
    return this;
  }

  public CredentialCacheBuilder maxCacheSize(long maxCacheSize) {
    this.maxCacheSize = maxCacheSize;
    return this;
  }

  public CredentialCacheBuilder cacheEntryTimeout(Duration cacheEntryTimeout) {
    this.cacheEntryTimeout = cacheEntryTimeout;
    return this;
  }

  /**
   * Builds a {@link LoadingCache} that loads {@link Credential}s from the datastore known by the
   * {@link AuthorizationCodeFlow}.  Before returning any discovered {@link Credential}s, calls
   * {@link Credential#refreshToken} if it looks like it is about to expire.
   *
   * @return a {@link LoadingCache} from {@link String} to {@link Credential} that knows how to refresh {@link Credential}s
   */
  public LoadingCache<String, Credential> build() {
    checkNotNull(authorizationCodeFlow);
    checkState(maxCacheSize > 0, "maxCacheSize <= 0");
    checkNotNull(cacheEntryTimeout);
    checkState(!(cacheEntryTimeout.isNegative() || cacheEntryTimeout.isZero()), "cacheEntryTimeout must be positive");
    return CacheBuilder.newBuilder()
        .maximumSize(maxCacheSize)
        .expireAfterWrite(cacheEntryTimeout)
        .build(new CacheLoader<String, Credential>() {
          @Override
          public Credential load(@Nonnull String userId) throws Exception {
            Credential credential = authorizationCodeFlow.loadCredential(userId);
            credential.refreshToken();
            return credential;
          }
        });
  }
}
