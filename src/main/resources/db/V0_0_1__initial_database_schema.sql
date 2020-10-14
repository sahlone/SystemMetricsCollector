-- noinspection SqlNoDataSourceInspectionForFile

CREATE TABLE system_metrics
(
    sm_id           UUID PRIMARY KEY,
    sm_metrics_name VARCHAR(255)             NOT NULL,
    sm_metric_value DECIMAL(30, 15)          NOT NULL,
    sm_event_time   TIMESTAMP WITH TIME ZONE NOT NULL,
    _event_time     TIMESTAMP WITH TIME ZONE NOT NULL default now()
);

CREATE INDEX ON system_metrics (sm_metrics_name, sm_metric_value);
CREATE INDEX ON system_metrics (sm_event_time);
