package com.gymcrm.dao;

import com.gymcrm.model.Training;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public class TrainingDao extends AbstractHibernateDao<Training, Long> {

    public TrainingDao(SessionFactory sessionFactory) {
        super(sessionFactory, Training.class);
    }

    public List<Training> findByTraineeCriteria(String traineeUsername, Date from, Date to, String trainerName, String trainingType) {
        StringBuilder hql = new StringBuilder("select t from Training t where t.trainee.user.username = :username");
        if (from != null) hql.append(" and t.trainingDate >= :from");
        if (to != null) hql.append(" and t.trainingDate <= :to");
        if (trainerName != null && !trainerName.isBlank()) hql.append(" and t.trainer.user.lastName like :trainerName");
        if (trainingType != null && !trainingType.isBlank()) hql.append(" and t.trainingType.trainingTypeName = :trainingType");

        var query = currentSession()
                .createQuery(hql.toString(), Training.class)
                .setParameter("username", traineeUsername);
        if (from != null) query.setParameter("from", from);
        if (to != null) query.setParameter("to", to);
        if (trainerName != null && !trainerName.isBlank()) query.setParameter("trainerName", "%" + trainerName + "%");
        if (trainingType != null && !trainingType.isBlank()) query.setParameter("trainingType", trainingType);

        return query.list();
    }

    public List<Training> findByTrainerCriteria(String trainerUsername, Date from, Date to, String traineeName) {
        StringBuilder hql = new StringBuilder("select t from Training t where t.trainer.user.username = :username");
        if (from != null) hql.append(" and t.trainingDate >= :from");
        if (to != null) hql.append(" and t.trainingDate <= :to");
        if (traineeName != null && !traineeName.isBlank()) hql.append(" and t.trainee.user.lastName like :traineeName");

        var query = currentSession()
                .createQuery(hql.toString(), Training.class)
                .setParameter("username", trainerUsername);
        if (from != null) query.setParameter("from", from);
        if (to != null) query.setParameter("to", to);
        if (traineeName != null && !traineeName.isBlank()) query.setParameter("traineeName", "%" + traineeName + "%");

        return query.list();
    }
}