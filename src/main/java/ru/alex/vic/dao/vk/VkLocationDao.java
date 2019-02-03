package ru.alex.vic.dao.vk;

import com.google.inject.Provider;
import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.vk.VkLocation;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;

@Singleton
public class VkLocationDao implements Dao<Long, VkLocation> {
    private final Provider<EntityManager> em;

    @Inject
    public VkLocationDao(Provider<EntityManager> em) {
        this.em = em;
    }

    @Override
    public Class<?> getEntityClass() {
        return VkLocation.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em.get();
    }
}
