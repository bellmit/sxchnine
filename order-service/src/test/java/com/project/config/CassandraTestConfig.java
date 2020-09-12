package com.project.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

@TestConfiguration
public class CassandraTestConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "test2";
    }

    protected boolean getMetricsEnabled() {
        return false;
    }

    @Override
    protected String getLocalDataCenter() {
        return "datacenter1";
    }

    @Override
    protected int getPort() {
        return 9142;
    }
}
