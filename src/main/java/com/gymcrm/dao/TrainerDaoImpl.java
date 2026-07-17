package com.gymcrm.dao;

import com.gymcrm.model.Trainer;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class TrainerDaoImpl extends AbstractHibernateDao<Trainer, Long> implements TrainerDao {

    public TrainerDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Trainer.class);
    }

    @Override
    public Optional<Trainer> findByUsername(String username) {
        return currentSession()
                .createQuery(
                        "select distinct t from Trainer t left join fetch t.trainees where t.username = :username",
                        Trainer.class
                )
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public List<Trainer> findUnassignedTrainersForTrainee(String traineeUsername) {
        String hql = """
                select tr from Trainer tr
                where tr.isActive = true
                and tr.id not in (
                    select t2.id from Trainee te join te.trainers t2
                    where te.username = :username
                )
                """;

        return currentSession()
                .createQuery(hql, Trainer.class)
                .setParameter("username", traineeUsername)
                .list();
    }

    @Override
    public Set<Trainer> findByUsernames(Set<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return Set.of();
        }

        return new HashSet<>(
                currentSession()
                        .createQuery("select t from Trainer t where t.username in (:usernames)", Trainer.class)
                        .setParameterList("usernames", usernames)
                        .list()
        );
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return currentSession()
                .createQuery(
                        "select distinct t from Trainer t " +
                                "left join fetch t.trainees " +
                                "where t.id = :id",
                        Trainer.class
                )
                .setParameter("id", id)
                .uniqueResultOptional();
    }
}