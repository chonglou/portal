package com.odong.core.config;

import com.jolbox.bonecp.BoneCPDataSource;
import com.odong.core.store.DbUtil;
import com.odong.core.store.Driver;
import httl.web.WebEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;

/**
 * Created by flamen on 13-12-30下午1:14.
 */
@Configuration("core.config.store")
@EnableTransactionManagement
public class Store {
        /*
    @Bean
    DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(jdbcDriver);
        dataSource.setUrl(jdbcUrl);
        dataSource.setUsername(jdbcUsername);
        dataSource.setPassword(jdbcPassword);
        return dataSource;
    }
    */


    @Bean(destroyMethod = "close")
    BoneCPDataSource getDataSource() {

        BoneCPDataSource ds = new BoneCPDataSource();

        ds.setDriverClass(jdbcDriver);
        ds.setJdbcUrl(jdbcUtil.getJdbcUrl());
        switch (jdbcDriver) {
            case Driver.MYSQL:
                ds.setUsername(jdbcUsername);
                ds.setPassword(jdbcPassword);
                break;
        }

        ds.setIdleConnectionTestPeriodInSeconds(60);
        ds.setIdleMaxAgeInSeconds(300);
        ds.setMaxConnectionsPerPartition(poolMaxConnectionsPerPartition);
        ds.setMinConnectionsPerPartition(poolMinConnectionsPerPartition);
        ds.setPartitionCount(poolPartitionCount);
        ds.setAcquireIncrement(5);
        ds.setStatementsCacheSize(poolStatementsCacheSize);
        ds.setReleaseHelperThreads(3);
        return ds;
    }

    @Bean(name = "core.db.txManager")
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(getDataSource());
        return manager;
    }

    @Value("${jdbc.driver}")
    private String jdbcDriver;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    @Value("${pool.max_connections_per_partition}")
    private int poolMaxConnectionsPerPartition;
    @Value("${pool.min_connections_per_partition}")
    private int poolMinConnectionsPerPartition;
    @Value("${pool.partition_count}")
    private int poolPartitionCount;
    @Value("${pool.statements_cache_size}")
    private int poolStatementsCacheSize;


    @Resource
    private DbUtil jdbcUtil;

    private final static Logger logger = LoggerFactory.getLogger(Store.class);

    public void setJdbcUtil(DbUtil jdbcUtil) {
        this.jdbcUtil = jdbcUtil;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public void setJdbcUsername(String jdbcUsername) {
        this.jdbcUsername = jdbcUsername;
    }

    public void setJdbcPassword(String jdbcPassword) {
        this.jdbcPassword = jdbcPassword;
    }

    public void setPoolMaxConnectionsPerPartition(int poolMaxConnectionsPerPartition) {
        this.poolMaxConnectionsPerPartition = poolMaxConnectionsPerPartition;
    }

    public void setPoolMinConnectionsPerPartition(int poolMinConnectionsPerPartition) {
        this.poolMinConnectionsPerPartition = poolMinConnectionsPerPartition;
    }

    public void setPoolPartitionCount(int poolPartitionCount) {
        this.poolPartitionCount = poolPartitionCount;
    }

    public void setPoolStatementsCacheSize(int poolStatementsCacheSize) {
        this.poolStatementsCacheSize = poolStatementsCacheSize;
    }


}
