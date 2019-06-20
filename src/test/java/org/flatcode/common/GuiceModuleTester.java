package org.flatcode.common;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.testing.fieldbinder.BoundFieldModule;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class providing a simple DSL for testing Guice modules.
 *
 * @author wgrim
 * @since 2019-06-21
 */
public class GuiceModuleTester {

  private Injector injector;

  public GuiceModuleTester(Module... modules) {
    List<Module> moduleArray = new ArrayList<Module>(modules);
    moduleArray.add(BoundFieldModule.of(this));
    injector = Guice.createInjector(moduleArray);
  }

  public GuiceModuleTester provides(Class<?> type) {
    injector.getInstance(type);
    return this;
  }

  public GuiceModuleTester provides(Key<?> key) {
    injector.getInstance(key);
    return this;
  }
}
