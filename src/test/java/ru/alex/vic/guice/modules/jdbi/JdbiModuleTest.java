package ru.alex.vic.guice.modules.jdbi;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import org.junit.Before;
import org.junit.Test;
import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.vk.VkLocation;

public class JdbiModuleTest {

    @Repository
    private Dao<Long, VkLocation> mergeService;

    @Before
    public void setUp() {
        Module module = new AbstractModule() {
            @Override
            protected void configure() {
               install(new JdbiModule());
            }
        };
        Guice.createInjector(module).injectMembers(this);
    }

    @Test
    public void test(){
        final VkLocation byId = mergeService.findById(1L);

    }

}