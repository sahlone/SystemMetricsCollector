package com.sahlone.app.smc.metrics.consumer;

import com.sahlone.app.smc.logging.Argument;
import com.sahlone.app.smc.logging.ContextualLogger;
import com.sahlone.app.smc.logging.TracingContext;
import com.sahlone.app.smc.models.MetricsHandlerResult;
import com.sahlone.app.smc.models.SystemMetrics;
import com.sahlone.app.smc.repository.MetricsRepository;
import org.jooq.exception.DataAccessException;

public class DatabaseMetricsHandler implements MetricsHandler {
  private ContextualLogger logger = new ContextualLogger(DatabaseMetricsHandler.class);
  private MetricsRepository metricsRepository;

  public DatabaseMetricsHandler(MetricsRepository metricsRepository) {
    this.metricsRepository = metricsRepository;
  }

  @Override
  public MetricsHandlerResult handle(TracingContext tracingContext, SystemMetrics metrics) {
    MetricsHandlerResult result;
    try {
      metricsRepository.insert(tracingContext, metrics);
      result = new MetricsHandlerResult(true, null);
    } catch (DataAccessException err) {
      logger.errorWithCause(
          tracingContext,
          err,
          "Error handling metrics data in handler",
          Argument.of("metrics", metrics));
      result = new MetricsHandlerResult(false, err);
    }
    return result;
  }
}
