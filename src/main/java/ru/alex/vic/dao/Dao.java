package ru.alex.vic.dao;

public interface Dao<I, T> {

    T save(T enity);

    T getById(I id);


    void deleteAll();


}
