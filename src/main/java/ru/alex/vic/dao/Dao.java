package ru.alex.vic.dao;


import com.google.inject.persist.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;


@SuppressWarnings("unchecked")
public interface Dao<I, T> {


    default List<T> getAll() {
        final TypedQuery<?> typedQuery = getTypedQuery(Collections.emptyMap(), PredicateType.EQUAL);
        return (List<T>) typedQuery.getResultList();
    }


    @SuppressWarnings("unchecked")
    default T findById(I id) {
        final EntityManager entityManager = getEntityManager();
        final Object obj = entityManager.find(getEntityClass(), id);
        return (T) obj;
    }


    default List<T> findByField(String fieldName, Object value) {
        TypedQuery<?> typed = getTypedQuery(Collections.singletonMap(fieldName, value), PredicateType.EQUAL);
        return (List<T>) typed.getResultList();
    }

    default List<T> findByFieldLike(String fieldName, String value) {
        TypedQuery<?> typed = getTypedQuery(Collections.singletonMap(fieldName, value), PredicateType.LIKE);
        return (List<T>) typed.getResultList();
    }


    default List<T> findByFields(Map<String, Object> params) {
        TypedQuery<?> typed = getTypedQuery(params, PredicateType.EQUAL);
        return (List<T>) typed.getResultList();
    }

    default T findByFieldSingle(String fieldName, Object value) {
        return (T) getTypedQuery(Collections.singletonMap(fieldName, value), PredicateType.EQUAL).getSingleResult();
    }


    @Transactional
    default T save(T enity) {
        final EntityManager entityManager = getEntityManager();
        entityManager.persist(enity);
        return enity;
    }

    @Transactional
    default void save(List<T> enities) {
        final EntityManager entityManager = getEntityManager();
        for (T enity : enities) {
            entityManager.persist(enity);
        }
    }

    @Transactional
    default <V> List<T> save(List<V> list, Function<V, T> func) {
        return list.stream().map(func).map(this::save).collect(Collectors.toList());
    }


    @Transactional
    default <V, P> List<T> save(List<V> list, P parent, BiFunction<P, V, T> func) {
        final EntityManager entityManager = getEntityManager();
        return list.stream().map(v -> func.apply(parent, v)).map(this::save).collect(Collectors.toList());
    }


    @Transactional
    default void update(T entitie) {
        getEntityManager().merge(entitie);
    }


    @Transactional
    default void update(List<T> entities, Consumer<T> consumer) {
        final EntityManager entityManager = getEntityManager();
        for (T entity : entities) {
            consumer.accept(entity);
            entityManager.merge(entity);
        }
    }


    @Transactional
    default void delete(T entity) {
        final EntityManager entityManager = getEntityManager();
        entityManager.remove(entity);
    }

    @Transactional
    default void delete(List<T> entities) {
        final EntityManager entityManager = getEntityManager();
        for (T entity : entities) {
            entityManager.remove(entity);
        }
    }


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

    enum PredicateType {
        EQUAL, LIKE
    }

    default TypedQuery<?> getTypedQuery(Map<String, Object> params, PredicateType queryType) {
        final EntityManager entityManager = getEntityManager();
        final Class<?> entityClass = getEntityClass();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery criteria = builder.createQuery(entityClass);
        Root<?> from = criteria.from(entityClass);
        criteria.select(from);
        Predicate[] predicates = new Predicate[params.size()];
        int count = 0;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            switch (queryType) {
                case EQUAL:
                    predicates[count] = builder.equal(from.get(entry.getKey()), entry.getValue());
                    break;
                case LIKE:
                    predicates[count] = builder.like(from.get(entry.getKey()), entry.getValue().toString());
                    break;
            }
            count++;
        }
        criteria.where(predicates);
        return entityManager.createQuery(criteria);
    }


}
