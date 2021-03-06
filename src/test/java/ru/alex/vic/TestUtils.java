package ru.alex.vic;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.name.Names;
import org.mockito.Mockito;
import ru.alex.vic.dao.Dao;

import javax.persistence.EntityManager;
import java.util.Properties;

import static ru.alex.vic.MyGuiceServletConfig.APPLICATION_PROPERTIES;

public class TestUtils {

    public static Module getTestModule(Class<?>... bindings) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                final Properties properties = Utils.createProperties(APPLICATION_PROPERTIES);
                Names.bindProperties(binder(), properties);
                for (Class<?> binding : bindings) {
                    bind(binding);
                }
                bind(Dao.class).toInstance(Mockito.mock(Dao.class));

            }
        };
    }

    public static void createInjector(Object obj, Class<?>... bindings) {
        Guice.createInjector(getTestModule(bindings)).injectMembers(obj);
    }
}
