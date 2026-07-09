package com.gymcrm.dao;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class AbstractHibernateDao<T, ID> {

    protected final SessionFactory sessionFactory;
    private final Class<T> entityClass;

    protected AbstractHibernateDao(SessionFactory sessionFactory, Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    protected Session currentSession() {
        return sessionFactory.getCurrentSession();
    }

    public T save(T entity) {
        currentSession().persist(entity);
        log.debug("Persisted entity: {}", entityClass.getSimpleName());
        return entity;
    }

    public T update(T entity) {
        @SuppressWarnings("unchecked")
        T merged = (T) currentSession().merge(entity);
        log.debug("Updated entity: {}", entityClass.getSimpleName());
        return merged;
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(currentSession().get(entityClass, (java.io.Serializable) id));
    }

    public List<T> findAll() {
        return currentSession()
                .createQuery("from " + entityClass.getSimpleName(), entityClass)
                .list();
    }

    public void delete(T entity) {
        currentSession().remove(entity);
        log.debug("Deleted entity: {}", entityClass.getSimpleName());
    }
}