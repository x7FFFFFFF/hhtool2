package ru.alex.vic.guice.modules.datasource;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.sql.DataSource;
import java.io.Closeable;
import java.io.IOException;

@Singleton
public class ConnectionPoolService {

    private final Provider<DataSource> provider;
    private volatile DataSource dataSource;

    @Inject
    public ConnectionPoolService(Provider<DataSource> dataSource) {
        this.provider = dataSource;
    }


    public DataSource start() {
        if (this.dataSource != null) {
            throw new IllegalStateException();
        }
        this.dataSource = provider.get();
        return dataSource;
    }


    public void stop() {
        if (dataSource instanceof Closeable) {
            try {
                ((Closeable) dataSource).close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
