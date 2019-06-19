// Copyright 2018 FantasyRank. All rights reserved.
package org.flatcode.servlet;

import java.util.Optional;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.google.common.util.concurrent.ServiceManager;
import com.google.inject.Injector;
import com.sun.jersey.spi.container.servlet.ServletContainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link ServletContextListener} that ties the {@link ServletContainer} lifecycle to the internal service's' lifecyles.
 *
 * @author wgrim
 * @since 2018-10-09
 */
public class ServiceManagerServletContextListener implements ServletContextListener {

  private static final Logger log = LoggerFactory.getLogger(ServiceManagerServletContextListener.class);

  private Optional<ServiceManager> serviceManager = Optional.empty();

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    log.info("Application services starting");
    ServletContext context = sce.getServletContext();
    Injector injector = (Injector) context.getAttribute(Injector.class.getName());
    try {
      serviceManager = Optional.of(injector.getInstance(ServiceManager.class));
      serviceManager.get().startAsync().awaitHealthy();
    } catch (Exception e) {
      log.info("No services found; so, none started");
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    if (serviceManager.isPresent()) {
      log.info("Application services stopping");
      serviceManager.get().stopAsync().awaitStopped();
      log.info("Application services stopped");
    }
  }
}
