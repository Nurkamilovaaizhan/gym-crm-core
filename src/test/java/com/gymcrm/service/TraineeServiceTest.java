package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.UserDao;
import com.gymcrm.model.Trainee;
import com.gymcrm.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock private TraineeDao traineeDao;
    @Mock private UserDao userDao;
    @Mock private AuthenticationService authenticationService;

    @InjectMocks private TraineeService traineeService;

    private Trainee trainee;
    private User concreteUser;

    @BeforeEach
    void setUp() {
        // Убрали {} — теперь User создается как обычный объект
        concreteUser = new User();
        concreteUser.setFirstName("Alan");
        concreteUser.setLastName("Walker");

        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUser(concreteUser);
    }

    @Test
    void testCreateTrainee_ShouldGenerateCredentials() {
        Set<String> existingUsernames = new HashSet<>();
        when(userDao.findAllUsernames()).thenReturn(existingUsernames);
        when(traineeDao.save(any(Trainee.class))).thenReturn(trainee);

        Trainee created = traineeService.createTrainee(trainee);

        assertNotNull(created);
        assertEquals("Alan.Walker", created.getUser().getUsername());
        assertNotNull(created.getUser().getPassword());
        verify(traineeDao, times(1)).save(trainee);
    }

    @Test
    void testGetTraineeByUsername_ShouldAuthenticateAndReturnTrainee() {
        String username = "alan.walker";
        String password = "password123";

        doNothing().when(authenticationService).authenticate(username, password);
        when(traineeDao.findByUsername(username)).thenReturn(Optional.of(trainee));

        Optional<Trainee> found = traineeService.getTraineeByUsername(username, password);

        assertTrue(found.isPresent());
        verify(traineeDao, times(1)).findByUsername(username);
    }

    @Test
    void testDeleteTraineeByUsername_ShouldAuthenticateAndDelete() {
        String username = "alan.walker";
        String password = "password123";

        doNothing().when(authenticationService).authenticate(username, password);
        doNothing().when(traineeDao).deleteByUsername(username);

        traineeService.deleteTraineeByUsername(username, password);

        verify(traineeDao, times(1)).deleteByUsername(username);
    }
}