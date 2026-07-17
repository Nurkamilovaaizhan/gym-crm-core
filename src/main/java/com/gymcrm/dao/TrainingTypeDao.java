package com.gymcrm.dao;

import com.gymcrm.model.TrainingType;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class TrainingTypeDao extends AbstractHibernateDao<TrainingType, Long> {

    public TrainingTypeDao(SessionFactory sessionFactory) {
        super(sessionFactory, TrainingType.class);
    }
}