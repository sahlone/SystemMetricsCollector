package com.sahlone.app.smc.repository;

import com.sahlone.app.smc.logging.Argument;
import com.sahlone.app.smc.logging.ContextualLogger;
import com.sahlone.app.smc.logging.TracingContext;
import com.sahlone.app.smc.models.SystemMetrics;
import com.sahlone.app.smc.repository.tables.SystemMetricsTable;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.exception.DataAccessException;
import org.jooq.impl.DSL;

public class MetricsRepository {
  private ContextualLogger logger = new ContextualLogger((MetricsRepository.class));
  private final DataSource dataSource;
  private final DSLContext dslContext;

  public MetricsRepository(DataSource dataSource) {
    this.dataSource = dataSource;
    this.dslContext = DSL.using(dataSource.getDataSource(), SQLDialect.POSTGRES);
  }

  public void insert(TracingContext tracingContext, SystemMetrics metrics)
      throws DataAccessException {
    try {
      dslContext
          .insertInto(
              SystemMetricsTable.metricTable,
              SystemMetricsTable.metricId,
              SystemMetricsTable.metricName,
              SystemMetricsTable.metricValue,
              SystemMetricsTable.metricTime)
          .values(
              metrics.getTraceId(),
              metrics.getLabel().name,
              metrics.getValue(),
              metrics.getSampleTime())
          .execute();
      logger.debug(
          tracingContext, "Metrics persisted successfully", Argument.of("metrics", metrics));
    } catch (DataAccessException err) {
      logger.errorWithCause(
          tracingContext,
          err,
          "Error persisting metrics to database",
          Argument.of("metrics", metrics));
      throw err;
    }
  }
}
