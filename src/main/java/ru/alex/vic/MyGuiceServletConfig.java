package ru.alex.vic;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import ru.alex.vic.dao.Dao;
import ru.alex.vic.dao.hh.HHLocationDao;
import ru.alex.vic.entities.hh.HHLocation;

public class MyGuiceServletConfig extends GuiceServletContextListener {
    protected Injector getInjector() {
        final ResourceConfig rc = new PackagesResourceConfig("ru.alex.vic.rest");
        return Guice.createInjector(new ServletModule() {

            @Override
            protected void configureServlets() {
                install(new JpaPersistModule("my-persistence-unit"));
                filter("/*").through(PersistFilter.class);

                bind(new TypeLiteral<Dao<Long, HHLocation>>() {
                }).to(HHLocationDao.class);

                for (Class<?> resource : rc.getClasses()) {
                    System.out.println("Binding resource: " + resource.getName());
                    bind(resource);
                }

                serve("/services/*").with(GuiceContainer.class);
            }
        });
    }
}
