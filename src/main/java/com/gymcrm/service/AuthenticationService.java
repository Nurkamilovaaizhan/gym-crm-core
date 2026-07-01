package com.gymcrm.service;

import com.gymcrm.dao.UserDao;
import com.gymcrm.exception.AuthenticationException;
import com.gymcrm.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;

    public void authenticate(String username, String password) {
        User user = userDao.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Authentication failed: user {} not found", username);
                    return new AuthenticationException("Invalid username or password");
                });

        if (!user.getPassword().equals(password)) {
            log.warn("Authentication failed: wrong password for user {}", username);
            throw new AuthenticationException("Invalid username or password");
        }
        log.info("User {} authenticated successfully", username);
    }
}