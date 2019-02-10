package ru.alex.vic.guice.modules.datasource;

import com.google.inject.Singleton;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.util.Properties;

@Singleton
public class HikariDataSourceProvider implements Provider<DataSource> {

    private final HikariConfig config;

    @Inject
    public HikariDataSourceProvider(@DbProperties Properties config) {
        this.config = new HikariConfig(config);
    }

    @Override
    public DataSource get() {
        return new HikariDataSource(config);
    }
}
