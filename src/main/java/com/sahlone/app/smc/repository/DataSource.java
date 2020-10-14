package com.sahlone.app.smc.repository;

import com.sahlone.app.smc.repository.models.DatabaseConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSource {

  private final DatabaseConfig config;

  private final HikariDataSource delegateDs;

  public DataSource(DatabaseConfig config) {
    this.config = config;
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setJdbcUrl(config.getJdbcUrl());
    hikariConfig.setUsername(config.getUsername());
    hikariConfig.setPassword(config.getPassword());
    delegateDs = new HikariDataSource(hikariConfig);
  }

  public DatabaseConfig getConfig() {
    return config;
  }

  public javax.sql.DataSource getDataSource() {
    return delegateDs;
  }
}
