package ru.alex.vic.guice.modules.jdbi;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import java.lang.reflect.Field;

public class RepositoryTypeListener implements TypeListener {


    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        Class<?> clazz = type.getRawType();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getType().isInterface() &&
                        field.isAnnotationPresent(Repository.class)) {
                    encounter.register(new RepositoryMembersInjector<I>(field));
                }
            }
            clazz = clazz.getSuperclass();
        }
    }


}
