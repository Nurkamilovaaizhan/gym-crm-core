package com.gymcrm.dao;
import com.gymcrm.model.User;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository public class UserDao extends AbstractHibernateDao<User, Long> {
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }

    public Optional<User> findByUsername(String username) {
        return currentSession()
                .createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username).uniqueResultOptional();
    }

    public Set<String> findAllUsernames() {
        List<String> usernames = currentSession()
                .createQuery("select u.username from User u", String.class).list();
        return usernames.stream().collect(Collectors.toSet());
    }
}