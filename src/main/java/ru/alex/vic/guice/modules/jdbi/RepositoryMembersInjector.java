package ru.alex.vic.guice.modules.jdbi;

import com.google.inject.MembersInjector;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.config.ConfigRegistry;
import org.jdbi.v3.core.mapper.RowMappers;
import org.jdbi.v3.core.mapper.reflect.BeanMapper;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class RepositoryMembersInjector<I> implements MembersInjector<I> {
    private Field field;
    private Object proxy;

    public RepositoryMembersInjector(Field field, Jdbi jdbi) {
        this.field = field;
        final Type genericType = this.field.getGenericType();
        final Type[] actualTypeArguments = ((ParameterizedTypeImpl) genericType).getActualTypeArguments();
        final ConfigRegistry config = jdbi.getConfig();
        final RowMappers rowMappers = config.get(RowMappers.class);
        try {
            rowMappers.register(BeanMapper.factory(Class.forName(actualTypeArguments[1].getTypeName())));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }
        proxy = jdbi.onDemand(field.getType());
        this.field.setAccessible(true);
    }

    @Override
    public void injectMembers(Object instance) {
        try {
            field.set(instance, proxy);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
