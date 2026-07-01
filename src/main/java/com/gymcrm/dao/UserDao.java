package com.gymcrm.dao;

import com.gymcrm.model.User;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class UserDao extends AbstractHibernateDao<User, Long> {

    public UserDao() {
        super(User.class);
    }

    public Optional<User> findByUsername(String username) {
        return sessionFactory.getCurrentSession()
                .createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .uniqueResultOptional();
    }

    public Set<String> findAllUsernames() {
        List<String> usernames = sessionFactory.getCurrentSession()
                .createQuery("select u.username from User u", String.class)
                .list();
        return usernames.stream().collect(Collectors.toSet());
    }
}