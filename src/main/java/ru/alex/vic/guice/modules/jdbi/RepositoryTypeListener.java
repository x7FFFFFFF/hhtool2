package ru.alex.vic.guice.modules.jdbi;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.ProvisionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObject;

import java.lang.reflect.Field;

public class RepositoryTypeListener implements TypeListener {


    private final Jdbi jdbi;

    public RepositoryTypeListener(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        Class<?> clazz = type.getRawType();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                final Class<?> fieldType = field.getType();
                if (fieldType.isInterface() &&
                        (SqlObject.class.isAssignableFrom(fieldType) || fieldType.isAnnotationPresent(Repository.class))) {
                    encounter.register(new RepositoryMembersInjector<>(field, jdbi));
                }
            }
            clazz = clazz.getSuperclass();
        }
    }


}
