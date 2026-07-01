package com.gymcrm.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractHibernateDao<T, ID> implements GenericDao<T, ID> {

    @Autowired
    protected SessionFactory sessionFactory;

    private final Class<T> entityClass;

    protected AbstractHibernateDao(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public T save(T entity) {
        sessionFactory.getCurrentSession().persist(entity);
        log.debug("Persisted entity: {}", entityClass.getSimpleName());
        return entity;
    }

    @Override
    public T update(T entity) {
        T merged = sessionFactory.getCurrentSession().merge(entity);
        log.debug("Updated entity: {}", entityClass.getSimpleName());
        return merged;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(entityClass, (java.io.Serializable) id));
    }

    @Override
    public List<T> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from " + entityClass.getSimpleName(), entityClass)
                .list();
    }

    @Override
    public void delete(T entity) {
        sessionFactory.getCurrentSession().remove(entity);
        log.debug("Deleted entity: {}", entityClass.getSimpleName());
    }
}