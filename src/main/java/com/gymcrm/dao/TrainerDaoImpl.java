package com.gymcrm.dao;
import com.gymcrm.model.Trainer;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import java.util.List; import java.util.Optional;

@Repository
public class TrainerDaoImpl extends AbstractHibernateDao<Trainer, Long> implements TrainerDao {
    public TrainerDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory, Trainer.class);
    }
    @Override
    public Optional<Trainer> findByUsername(String username) {
        return currentSession()
                .createQuery("select t from Trainer t where t.user.username = :username", Trainer.class)
                .setParameter("username", username)
                .uniqueResultOptional();    }

    @Override
    public List<Trainer> findUnassignedTrainersForTrainee(String traineeUsername) {
        String hql = "select tr from Trainer tr where tr.id not in (" + "select t2.id from Trainee te join te.trainers t2 where te.user.username = :username" + ")";
        return currentSession()
                .createQuery(hql, Trainer.class)
                .setParameter("username", traineeUsername).list();
    }
}