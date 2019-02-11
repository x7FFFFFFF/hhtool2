package ru.alex.vic.guice.modules.jdbi;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import org.junit.Before;
import org.junit.Test;
import ru.alex.vic.Utils;
import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.vk.VkLocation;

import java.util.List;
import java.util.Properties;

import static ru.alex.vic.MyGuiceServletConfig.APPLICATION_PROPERTIES;

public class JdbiModuleTest {

    @Repository
    private TestCrudRepo<Long, User> userTestCrudRepo;

    @Before
    public void setUp() {
        Module module = new AbstractModule() {
            @Override
            protected void configure() {
                final Properties properties = Utils.createProperties(APPLICATION_PROPERTIES);
               install(new JdbiModule(properties));
            }
        };
        Guice.createInjector(module).injectMembers(this);
    }

    @Test
    public void test(){
        //final VkLocation byId = mergeService.findById(1L);
        final List<User> all = userTestCrudRepo.getAll();
        System.out.println("all = " + all);

    }

}