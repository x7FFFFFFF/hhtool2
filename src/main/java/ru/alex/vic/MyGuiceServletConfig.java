package ru.alex.vic;

import com.google.gson.Gson;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import ru.alex.vic.client.HttpClient;
import ru.alex.vic.client.hh.HHClient;
import ru.alex.vic.dao.Dao;
import ru.alex.vic.dao.hh.HHLocationDao;
import ru.alex.vic.dao.merge.MergeVkDao;
import ru.alex.vic.dao.vk.VkLocationDao;
import ru.alex.vic.entities.hh.HHLocation;
import ru.alex.vic.entities.merge.MergeVk;
import ru.alex.vic.entities.vk.VkLocation;
import ru.alex.vic.guice.modules.jdbi.JdbiModule;

import java.util.Properties;

public class MyGuiceServletConfig extends GuiceServletContextListener {

    public static final String APP_PERSISTENCE_UNIT = "app.persistence.unit";
    public static final String APPLICATION_PROPERTIES = "application.properties";
    public static final String REST_PACKAGE = "ru.alex.vic.rest";

    protected Injector getInjector() {
        final ResourceConfig rc = new PackagesResourceConfig(REST_PACKAGE);
        return Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {
                final Properties properties = Utils.createProperties(APPLICATION_PROPERTIES);
                Names.bindProperties(binder(), properties);
                install(new JdbiModule(properties));
                install(new JpaPersistModule(properties.getProperty(APP_PERSISTENCE_UNIT)));
                filter("/*").through(PersistFilter.class);

                bind(new TypeLiteral<Dao<Long, HHLocation>>() {
                }).to(HHLocationDao.class);
                bind(new TypeLiteral<Dao<Long, VkLocation>>() {
                }).to(VkLocationDao.class);
                bind(new TypeLiteral<Dao<Long, MergeVk>>() {
                }).to(MergeVkDao.class);

                for (Class<?> resource : rc.getClasses()) {
                    System.out.println("Binding resource: " + resource.getName());
                    bind(resource);
                }
                bind(HttpClient.class);
                bind(HHClient.class);
                bind(VkLocationDao.class);
                bind(Gson.class);
                serve("/services/*").with(GuiceContainer.class);
                serve("/main").with(MainServlet.class);
            }
        });
    }
}
