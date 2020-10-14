package com.sahlone.app.smc.repository.tables;

import org.jooq.Field;
import org.jooq.Table;
import org.jooq.impl.DSL;

public class SystemMetricsTable {

  public static Table metricTable = DSL.table("system_metrics");
  public static Field<Object> metricId = DSL.field("sm_id");
  public static Field<Object> metricName = DSL.field("sm_metrics_name");
  public static Field<Object> metricValue = DSL.field("sm_metric_value");
  public static Field<Object> metricTime = DSL.field("sm_event_time");
}
