package ru.alex.vic.guice.modules.jdbi;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

public class JdbiModule extends AbstractModule {

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new RepositoryTypeListener());
    }
}
