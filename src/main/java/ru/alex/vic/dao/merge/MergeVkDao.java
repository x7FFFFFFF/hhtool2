package ru.alex.vic.dao.merge;

import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.merge.MergeVk;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class MergeVkDao implements Dao<Long, MergeVk> {
    private final Provider<EntityManager> em;

    @Inject
    public MergeVkDao(Provider<EntityManager> em) {
        this.em = em;
    }

    @Override
    public Class<?> getEntityClass() {
        return MergeVk.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em.get();
    }
}