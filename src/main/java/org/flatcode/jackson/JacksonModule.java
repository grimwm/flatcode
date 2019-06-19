// Copyright 2018 FantasyRank. All rights reserved.
package org.flatcode.jackson;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Configures a Jackson {@link ObjectMapper}
 *
 * @author wgrim
 * @since 2018-10-09
 */
public class JacksonModule extends AbstractModule {

  @Override
  protected void configure() {
    super.configure();
  }

  @Provides @Singleton
  ObjectMapper provideObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper()
        .configure(MapperFeature.AUTO_DETECT_GETTERS, false)
        .configure(MapperFeature.AUTO_DETECT_SETTERS, true)
        .setSerializationInclusion(Include.NON_ABSENT)
        .registerModule(new GuavaModule())
        .registerModule(new Jdk8Module())
        .registerModule(new JodaModule())
        .registerModule(new JavaTimeModule())
        .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
        .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false);
    return objectMapper.setVisibility(objectMapper.getVisibilityChecker()
        .withFieldVisibility(Visibility.ANY)
        .withGetterVisibility(Visibility.NONE)
        .withIsGetterVisibility(Visibility.NONE)
        .withSetterVisibility(Visibility.NONE));
  }
}
