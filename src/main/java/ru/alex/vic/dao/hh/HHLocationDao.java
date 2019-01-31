package ru.alex.vic.dao.hh;

import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.hh.HHLocation;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class HHLocationDao implements Dao<Long, HHLocation> {
    @Inject
    private Provider<EntityManager> em;


    @Override
    public HHLocation save(HHLocation enity) {
        final EntityManager entityManager = em.get();
        entityManager.persist(enity);
        entityManager.flush();
        return enity;
    }

    @Override
    public HHLocation getById(Long id) {
        return em.get().find(HHLocation.class, id);
    }
}
