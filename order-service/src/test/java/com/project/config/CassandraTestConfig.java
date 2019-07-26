package com.project.config;

import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;

import java.io.IOException;

@TestConfiguration
public class CassandraTestConfig extends AbstractCassandraConfiguration {

    @Override
    protected String getKeyspaceName() {
        return "test2";
    }

    @Override
    protected boolean getMetricsEnabled() {
        return false;
    }

    @Override
    protected int getPort() {
        return 9142;
    }

    @Bean
    public CassandraClusterFactoryBean clusterFactoryBean() throws IOException, TTransportException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(EmbeddedCassandraServerHelper.DEFAULT_CASSANDRA_YML_FILE, 60000L);
        EmbeddedCassandraServerHelper.getCluster().getConfiguration().getSocketOptions().setReadTimeoutMillis(1000000);

        return this.cluster();
    }
}
