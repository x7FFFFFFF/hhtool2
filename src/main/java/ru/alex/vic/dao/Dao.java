package ru.alex.vic.dao;

import com.google.inject.persist.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaDelete;

public interface Dao<I, T> {

    @Transactional
    default void save(T enity) {
        final EntityManager entityManager = getEntityManager();
        entityManager.persist(enity);
    }
    @SuppressWarnings("unchecked")
    default T getById(I id) {
        final EntityManager entityManager = getEntityManager();
        final Object obj = entityManager.find(getEntityClass(), id);
        return (T) obj;
    }


    @SuppressWarnings("unchecked")
    @Transactional
    default void deleteAll() {
        final EntityManager entityManager = getEntityManager();
        final Class<?> entityClass = getEntityClass();
        final CriteriaDelete criteriaDelete = entityManager
                .getCriteriaBuilder().createCriteriaDelete(entityClass);
        criteriaDelete.from(entityClass);
        entityManager.createQuery(criteriaDelete).executeUpdate();
    }


    Class<?> getEntityClass();

    EntityManager getEntityManager();


}
