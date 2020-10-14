package com.sahlone.app.smc.repository;

import com.sahlone.app.smc.logging.Argument;
import com.sahlone.app.smc.logging.ContextualLogger;
import com.sahlone.app.smc.logging.TracingContext;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;

public class DatabaseMigrations {
  private final String location;
  private final DataSource dataSource;
  private final TracingContext tracingContext = TracingContext.of();
  private ContextualLogger logger = new ContextualLogger(DatabaseMigrations.class);

  public DatabaseMigrations(String location, DataSource dataSource) {
    this.location = location;
    this.dataSource = dataSource;
  }

  public void migrate() throws FlywayException {
    logger.debug(
        tracingContext,
        "Starting database migrations",
        Argument.of("dbConfig", dataSource.getConfig()));
    try {
      Flyway.configure()
          .baselineOnMigrate(false)
          .locations(location)
          .dataSource(dataSource.getDataSource())
          .load()
          .migrate();
    } catch (FlywayException err) {
      logger.errorWithCause(
          tracingContext,
          err,
          "Error while migrating database",
          Argument.of("dbConfig", dataSource.getConfig()));
      throw err;
    }
  }
}
