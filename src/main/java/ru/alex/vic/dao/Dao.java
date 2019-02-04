package ru.alex.vic.dao;

import com.google.inject.persist.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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
    default List<T> findByField(String fieldName, Object value) {
        final EntityManager entityManager = getEntityManager();
        final Class<?> entityClass = getEntityClass();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery criteria = builder.createQuery(entityClass);
        Root<?> from = criteria.from(entityClass);
        criteria.select(from);
        criteria.where(builder.equal(from.get(fieldName), value));
        TypedQuery<?> typed = entityManager.createQuery(criteria);
        return (List<T>) typed.getResultList();
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
