package ru.alex.vic.dao.hh;

import com.google.inject.Provider;
import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.hh.HHLocation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;


@Singleton
public class HHLocationDao implements Dao<Long, HHLocation> {

    private final Provider<EntityManager> em;

    @Inject
    public HHLocationDao(Provider<EntityManager> em) {
        this.em = em;
    }

    @Override
    public Class<?> getEntityClass() {
        return HHLocation.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em.get();
    }
}
