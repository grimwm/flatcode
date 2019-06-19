package org.flatcode.guice;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.util.Clock;
import com.google.inject.AbstractModule;

/**
 * Some bindings that happen to be used across the code but aren't specific to any type of usage scenarios.
 *
 * @author wgrim
 * @since 2018-12-04
 */
public class StandardBindingsModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(Clock.class).toInstance(Clock.SYSTEM);
    bind(HttpRequestFactory.class).toInstance(new NetHttpTransport().createRequestFactory());
  }
}
