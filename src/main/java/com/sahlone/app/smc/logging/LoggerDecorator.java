package com.sahlone.app.smc.logging;

import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.sahlone.app.smc.data.mapper.ObjectMapperFactory;
import net.logstash.logback.decorate.JsonFactoryDecorator;

public class LoggerDecorator implements JsonFactoryDecorator {

  @Override
  public MappingJsonFactory decorate(MappingJsonFactory factory) {
    factory.setCodec(ObjectMapperFactory.create());
    return factory;
  }
}
