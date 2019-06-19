// Copyright 2018 FantasyRank. All rights reserved.
package org.flatcode.oauth2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.flatcode.common.BaseObject;
import com.google.common.base.MoreObjects;

import static org.flatcode.common.MorePreconditions.checkNotNullOrEmpty;

/**
 * An object representing OAuth2 credentials.  This may be loaded from a JSON file.
 *
 * @author wgrim
 * @since 2018-10-14
 */
public class OAuth2ClientCredentials extends BaseObject {

  private final String applicationid;
  private final String clientId;
  private final String clientSecret;

  @JsonCreator
  private OAuth2ClientCredentials(Builder builder) {
    this.applicationid = checkNotNullOrEmpty(builder.applicationId);
    this.clientId = checkNotNullOrEmpty(builder.clientId);
    this.clientSecret = checkNotNullOrEmpty(builder.clientSecret);
  }

  @JsonGetter("app_id")
  public String getApplicationid() {
    return applicationid;
  }

  @JsonGetter("client_id")
  public String getClientId() {
    return clientId;
  }

  @JsonGetter("client_secret")
  public String getClientSecret() {
    return clientSecret;
  }

  @Override
  protected Object[] significantAttributes() {
    return new Object[]{applicationid, clientId, clientSecret};
  }

  @Override
  protected MoreObjects.ToStringHelper toStringHelper() {
    return super.toStringHelper()
        .add("applicationId", applicationid)
        .add("clientId", clientId)
        .add("clientSecret", clientSecret);
  }

  public static class Builder {

    private String applicationId;
    private String clientId;
    private String clientSecret;

    public Builder fromPrototype(OAuth2ClientCredentials credentials) {
      this.applicationId = credentials.applicationid;
      this.clientId = credentials.clientId;
      this.clientSecret = credentials.clientSecret;
      return this;
    }

    @JsonSetter("app_id")
    public Builder applicationId(String applicationId) {
      this.applicationId = applicationId;
      return this;
    }

    @JsonSetter("client_id")
    public Builder clientId(String clientId) {
      this.clientId = clientId;
      return this;
    }

    @JsonSetter("client_secret")
    public Builder clientSecret(String clientSecret) {
      this.clientSecret = clientSecret;
      return this;
    }

    public OAuth2ClientCredentials build() {
      return new OAuth2ClientCredentials(this);
    }
  }
}
