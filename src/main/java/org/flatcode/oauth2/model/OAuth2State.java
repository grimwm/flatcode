package org.flatcode.oauth2.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.flatcode.common.BaseObject;
import com.google.common.base.MoreObjects;

import java.net.URI;

import static org.flatcode.common.MorePreconditions.checkNotNullOrEmpty;

/**
 * Model for passing OAuth2 state info we need between external requests.
 *
 * @author wgrim
 * @since 2018-11-24
 */
public class OAuth2State extends BaseObject {

  private final String userId;
  private final String redirectUri;

  @JsonCreator
  public OAuth2State(Builder builder) {
    this.userId = checkNotNullOrEmpty(builder.userId);
    this.redirectUri = checkNotNullOrEmpty(builder.redirectUri);
  }

  @Override
  protected Object[] significantAttributes() {
    return new Object[]{userId, redirectUri};
  }

  @Override
  protected MoreObjects.ToStringHelper toStringHelper() {
    return super.toStringHelper()
        .add("userId", userId)
        .add("redirectUri", redirectUri);
  }

  @JsonGetter("user_id")
  public String getUserId() {
    return userId;
  }

  @JsonGetter("redirect_uri")
  public String getRedirectUri() {
    return redirectUri;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static Builder builder(OAuth2State state) {
    return builder().fromPrototype(state);
  }

  public static class Builder {

    private String userId;
    private String redirectUri;

    public Builder fromPrototype(OAuth2State state) {
      this.userId = state.userId;
      this.redirectUri = state.redirectUri;
      return this;
    }

    @JsonSetter("user_id")
    public Builder userId(String userId) {
      this.userId = userId;
      return this;
    }

    public Builder redirectUri(URI redirectUri) {
      return this.redirectUri(redirectUri.toString());
    }

    @JsonSetter("redirect_uri")
    public Builder redirectUri(String redirectUri) {
      this.redirectUri = redirectUri;
      return this;
    }

    public OAuth2State build() {
      return new OAuth2State(this);
    }
  }
}
