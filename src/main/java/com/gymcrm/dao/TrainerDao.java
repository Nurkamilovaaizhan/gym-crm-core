package com.gymcrm.dao;

import com.gymcrm.model.Trainer;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class TrainerDao extends AbstractHibernateDao<Trainer, Long> {

    public TrainerDao() {
        super(Trainer.class);
    }

    public Optional<Trainer> findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("select t from Trainer t where t.user.username = :username", Trainer.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public List<Trainer> findUnassignedTrainersForTrainee(String traineeUsername) {
        return sessionFactory.getCurrentSession()
                .createQuery("""
                select tr from Trainer tr
                where tr not in (
                    select t2 from Trainee te join te.trainers t2
                    where te.user.username = :username
                )""", Trainer.class)
                .setParameter("username", traineeUsername)
                .list();
    }
}