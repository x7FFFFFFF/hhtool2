package ru.alex.vic.dao.hh;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import ru.alex.vic.dao.Dao;
import ru.alex.vic.entities.hh.HHLocation;

import javax.inject.Inject;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;


@Singleton
public class HHLocationDao implements Dao<Long, HHLocation> {

    private final Provider<EntityManager> em;

    @Inject
    public HHLocationDao(Provider<EntityManager> em) {
        this.em = em;
    }

    @Override
    @Transactional
    public HHLocation save(HHLocation enity) {
        final EntityManager entityManager = em.get();
        entityManager.persist(enity);
        //entityManager.flush();
        return enity;
    }

    @Override
    public HHLocation getById(Long id) {
        return em.get().find(HHLocation.class, id);
    }

    @Override
    @Transactional
    public void deleteAll() {
        final CriteriaDelete<HHLocation> criteriaDelete = this.em.get()
                .getCriteriaBuilder().createCriteriaDelete(HHLocation.class);
        criteriaDelete.from(HHLocation.class);
        em.get().createQuery(criteriaDelete).executeUpdate();
    }
}
