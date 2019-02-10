package ru.alex.vic.guice.modules.jdbi;

import com.google.inject.MembersInjector;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class RepositoryMembersInjector<I> implements MembersInjector<I> {
    private Field field;

    public RepositoryMembersInjector(Field field) {
        this.field = field;
        final Type genericType = this.field.getGenericType();
        final Type[] actualTypeArguments = ((ParameterizedTypeImpl) genericType).getActualTypeArguments();
        System.out.println("actualTypeArguments = " + actualTypeArguments);
        this.field.setAccessible(true);
    }

    @Override
    public void injectMembers(Object instance) {
        System.out.println("instance = " + instance);

    }
}
