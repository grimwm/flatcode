package org.flatcode.net;

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

/**
 * Set of utility methods for making it easier to do common things with {@link UriBuilder}.
 *
 * @author wgrim
 * @since 2018-11-24
 */
public class UriBuilderUtils {

  /**
   * Creates a {@link UriBuilder} from the given information, creating a base URI
   * along with the basic path to the {@code resource} and {@code method}.
   *
   * @param uriInfo  {@link UriInfo} containing the base URI information
   * @param resource a named resource class
   * @param method   a method in the {@code resource}
   * @return a {@link UriBuilder} containing the base URI and path to the {@code resource}
   *         and {@code method}.
   *
   * @see UriInfo#getBaseUriBuilder()
   */
  public static UriBuilder uriBuilder(UriInfo uriInfo, Class resource, String method) {
    return uriInfo.getBaseUriBuilder().path(resource).path(resource, method);
  }
}
