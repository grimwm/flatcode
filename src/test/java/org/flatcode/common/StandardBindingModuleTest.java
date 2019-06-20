package org.flatcode.common;

import com.google.api.client.util.Clock;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.testing.fieldbinder.BoundFieldModule;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

/**
 * @author wgrim
 * @since 2019-06-20
 */
public class StandardBindingModuleTest {

  new GuiceModuleTester(new StandardBindingsModule());

  @Test
  public void provides() {
    Injector injector = Guice.createInjector(new StandardBindingsModule(), BoundFieldModule.of(this));
    injector.getInstance(Key.get(Clock.class));
  }
}
