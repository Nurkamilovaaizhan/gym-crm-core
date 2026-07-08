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
                .createQuery("select t from Trainee t where t.user.username = :username", Trainee.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    @Override
    public void deleteByUsername(String username) {
        Optional<Trainee> opt = findByUsername(username);
        opt.ifPresent(currentSession()::remove);
    }
}