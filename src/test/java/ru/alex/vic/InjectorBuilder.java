package ru.alex.vic;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import org.mockito.Mockito;

import java.util.*;

import static ru.alex.vic.MyGuiceServletConfig.APPLICATION_PROPERTIES;

public class InjectorBuilder {
    private Object object;

    public InjectorBuilder(Object object) {
        this.object = object;
    }

    private final List<Class<?>> classBindings = new ArrayList<>();
    private final Map<TypeLiteral, Object> mockedTypeLiterals = new HashMap<>();

    public <T> InjectorBuilder mock(TypeLiteral<T> typeLiteral) {
        mockedTypeLiterals.put(typeLiteral, Mockito.mock(typeLiteral.getRawType()));
        return this;
    }

    public InjectorBuilder mock(TypeLiteral... typeLiterals) {
        for (TypeLiteral typeLiteral : typeLiterals) {
            mock(typeLiteral);
        }
        return this;
    }


    public <T> InjectorBuilder bind(Class<T> clz) {
        classBindings.add(clz);
        return this;
    }


    public void create() {
        Module module = new AbstractModule() {
            @Override
            protected void configure() {
                final Properties properties = Utils.createProperties(APPLICATION_PROPERTIES);
                Names.bindProperties(binder(), properties);
                for (Class<?> binding : classBindings) {
                    bind(binding);
                }
                mockedTypeLiterals.forEach((k, v) -> {
                    bind(k).toInstance(v);
                });

            }
        };

        Guice.createInjector(module).injectMembers(object);
    }


}
