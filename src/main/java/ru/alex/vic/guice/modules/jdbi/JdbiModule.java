package ru.alex.vic.guice.modules.jdbi;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import javax.sql.DataSource;
import java.util.Properties;

import static ru.alex.vic.Utils.filterPropsStartWith;

public class JdbiModule extends AbstractModule {

    private final Jdbi jdbi;
    private final DataSource dataSource;

    public JdbiModule(Properties properties) {
        final String active = properties.getProperty("dataSource.active.profile", "");
        final Properties config = filterPropsStartWith(properties, active);
        final HikariConfig hikariConfig = new HikariConfig(config);
        dataSource = new HikariDataSource(hikariConfig); //TODO: close?
        jdbi =  Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
    }

    @Override
    protected void configure() {
        bind(DataSource.class).toInstance(dataSource);
        bind(Jdbi.class).toInstance(jdbi);
        bindListener(Matchers.any(), new RepositoryTypeListener(jdbi));
    }



}
