package com.gymcrm.dao;

import com.gymcrm.model.Trainee;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class TraineeDaoImpl extends AbstractHibernateDao<Trainee, Long> implements TraineeDao {

    public TraineeDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Trainee.class);
    }

    @Override
    public Optional<Trainee> findByUsername(String username) {
        return currentSession()
                .createQuery(
                        "select distinct t from Trainee t left join fetch t.trainers where t.username = :username",
                        Trainee.class
                )
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public void deleteByUsername(String username) {
        findByUsername(username).ifPresent(currentSession()::remove);
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return currentSession()
                .createQuery(
                        "select distinct t from Trainee t " +
                                "left join fetch t.trainers " +
                                "where t.id = :id",
                        Trainee.class
                )
                .setParameter("id", id)
                .uniqueResultOptional();
    }
}