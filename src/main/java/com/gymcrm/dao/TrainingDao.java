package com.gymcrm.dao;

import com.gymcrm.model.Training;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class TrainingDao extends AbstractHibernateDao<Training, Long> {

    public TrainingDao() {
        super(Training.class);
    }

    public List<Training> findByTraineeCriteria(String traineeUsername, Date from, Date to,
                                                String trainerName, String trainingType) {
        StringBuilder hql = new StringBuilder(
                "select t from Training t where t.trainee.user.username = :username");
        if (from != null) hql.append(" and t.trainingDate >= :from");
        if (to != null) hql.append(" and t.trainingDate <= :to");
        if (trainerName != null) hql.append(" and t.trainer.user.lastName like :trainerName");
        if (trainingType != null) hql.append(" and t.trainingType.trainingTypeName = :trainingType");

        var query = sessionFactory.getCurrentSession()
                .createQuery(hql.toString(), Training.class)
                .setParameter("username", traineeUsername);
        if (from != null) query.setParameter("from", from);
        if (to != null) query.setParameter("to", to);
        if (trainerName != null) query.setParameter("trainerName", "%" + trainerName + "%");
        if (trainingType != null) query.setParameter("trainingType", trainingType);

        return query.list();
    }
}