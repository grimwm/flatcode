// Copyright 2018 FantasyRank. All rights reserved.
package org.flatcode.servlet;

import java.util.Collection;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation that allows configuring the {@link Stage}.
 *
 * @author wgrim
 * @since 2018-10-09
 */
public abstract class AbstractGuiceServletContextListener extends GuiceServletContextListener {

  private static final Logger log = LoggerFactory.getLogger(AbstractGuiceServletContextListener.class);

  private final Stage DEFAULT_STAGE = Stage.DEVELOPMENT;

  @Override
  protected Injector getInjector() {
    return Guice.createInjector(stage(), modules());
  }

  protected abstract Collection<Module> modules();

  private Stage stage() {
    Stage stage = DEFAULT_STAGE;
    String stageType = System.getProperty("guice.stage", "");
    try {
      stage = Stage.valueOf(stageType);
    } catch (IllegalArgumentException e) {
      log.warn("Unable to load Stage for guice.version={}; defaulting to {}", stageType, DEFAULT_STAGE);
    }
    return stage;
  }
}
