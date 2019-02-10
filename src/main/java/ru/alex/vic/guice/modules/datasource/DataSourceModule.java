package ru.alex.vic.guice.modules.datasource;

import com.google.inject.AbstractModule;

import javax.sql.DataSource;
import java.util.Properties;

public class DataSourceModule extends AbstractModule {

    private final Properties properties;

    public DataSourceModule(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected void configure() {
        final String active = properties.getProperty("dataSource.active.profile", "");
        bind(Properties.class).annotatedWith(DbProperties.class).toInstance(getConfig(active));
        bind(DataSource.class).toProvider(HikariDataSourceProvider.class);//toConstructor(HikariDataSource.class.getConstructor(HikariConfig.class)).in(Singleton.class);
        bind(ConnectionPoolService.class);
    }

    private Properties getConfig(String active) {
        if (active.length() > 0) {
            Properties config = new Properties();
            properties.forEach((k, v) -> {
                String key = (String) k;
                if (key.startsWith(active)) {
                    config.setProperty(replace(key, active), (String) v);
                }
            });

            return config;
        }
        return properties;
    }

    private String replace(String key, String active) {
        return key.replace(active + ".", "");
    }

}
