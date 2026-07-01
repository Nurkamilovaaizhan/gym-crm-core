package com.gymcrm.dao;

import com.gymcrm.model.Trainee;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public class TraineeDao extends AbstractHibernateDao<Trainee, Long> {

    public TraineeDao() {
        super(Trainee.class);
    }

    public Optional<Trainee> findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("select t from Trainee t where t.user.username = :username", Trainee.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public void deleteByUsername(String username) {
        sessionFactory.getCurrentSession()
                .createQuery("delete from Trainee t where t.user.username = :username")
                .setParameter("username", username)
                .executeUpdate();
    }
}