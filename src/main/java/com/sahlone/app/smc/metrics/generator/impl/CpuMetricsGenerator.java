package com.sahlone.app.smc.metrics.generator.impl;

import com.sahlone.app.smc.metrics.generator.MetricsGenerator;
import com.sahlone.app.smc.models.SystemMetrics;
import com.sahlone.app.smc.models.SystemMetricsLabel;
import com.sun.management.OperatingSystemMXBean;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.List;
import javax.management.MBeanServerConnection;

public class CpuMetricsGenerator implements MetricsGenerator {

  MBeanServerConnection mgmtBean = ManagementFactory.getPlatformMBeanServer();

  private OperatingSystemMXBean osMBean;

  public CpuMetricsGenerator() throws IOException {
    this.osMBean =
        ManagementFactory.newPlatformMXBeanProxy(
            mgmtBean, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
  }

  @Override
  public List<SystemMetrics> generate() throws IOException {
    return List.of(
        new SystemMetrics(
            SystemMetricsLabel.CPU_TOTAL, Double.valueOf(osMBean.getAvailableProcessors())),
        new SystemMetrics(
            SystemMetricsLabel.CPU_USED, Double.valueOf(osMBean.getSystemLoadAverage())));
  }
}
