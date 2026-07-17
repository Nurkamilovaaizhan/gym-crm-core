package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.dao.UserDao;
import com.gymcrm.model.Trainee;
import com.gymcrm.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserDao userDao;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setFirstName("Alan");
        trainee.setLastName("Walker");
        trainee.setDateOfBirth(new Date());
        trainee.setAddress("Bishkek");
        trainee.setActive(true);
    }

    @Test
    void createTrainee_shouldGenerateUsernameAndPassword() {
        when(userDao.findAllUsernames()).thenReturn(new HashSet<>());
        when(traineeDao.save(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainee created = traineeService.createTrainee(trainee);

        assertNotNull(created);
        assertEquals("Alan.Walker", created.getUsername());
        assertNotNull(created.getPassword());
        assertEquals(10, created.getPassword().length());
        assertEquals("Alan", created.getFirstName());
        assertEquals("Walker", created.getLastName());

        verify(traineeDao, times(1)).save(any(Trainee.class));
    }

    @Test
    void getTraineeByUsername_shouldAuthenticateAndReturnTrainee() {
        trainee.setUsername("alan.walker");

        doNothing().when(authenticationService).authenticate("alan.walker", "pass123");
        when(traineeDao.findByUsername("alan.walker")).thenReturn(Optional.of(trainee));

        Optional<Trainee> found = traineeService.getTraineeByUsername("alan.walker", "pass123");

        assertTrue(found.isPresent());
        assertEquals("alan.walker", found.get().getUsername());
        verify(authenticationService, times(1)).authenticate("alan.walker", "pass123");
        verify(traineeDao, times(1)).findByUsername("alan.walker");
    }

    @Test
    void updateTrainee_shouldUpdateFields() {
        Trainee existing = new Trainee();
        existing.setId(1L);
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setDateOfBirth(new Date(0));
        existing.setAddress("Old address");
        existing.setActive(true);

        Trainee updated = new Trainee();
        updated.setId(1L);
        updated.setFirstName("New");
        updated.setLastName("Surname");
        updated.setDateOfBirth(new Date(1000));
        updated.setAddress("New address");
        updated.setActive(false);

        doNothing().when(authenticationService).authenticate("alan.walker", "pass123");
        when(traineeDao.findById(1L)).thenReturn(Optional.of(existing));
        when(traineeDao.update(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainee result = traineeService.updateTrainee("alan.walker", "pass123", updated);

        assertEquals("New", result.getFirstName());
        assertEquals("Surname", result.getLastName());
        assertEquals("New address", result.getAddress());
        assertFalse(result.isActive());

        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        verify(traineeDao).update(captor.capture());
        assertEquals("New", captor.getValue().getFirstName());
    }

    @Test
    void deleteTraineeByUsername_shouldAuthenticateAndDelete() {
        doNothing().when(authenticationService).authenticate("alan.walker", "pass123");
        doNothing().when(traineeDao).deleteByUsername("alan.walker");

        traineeService.deleteTraineeByUsername("alan.walker", "pass123");

        verify(traineeDao, times(1)).deleteByUsername("alan.walker");
    }

    @Test
    void updateTraineeTrainers_shouldReplaceTrainersSet() {
        Trainer trainer1 = new Trainer();
        trainer1.setId(10L);
        trainer1.setUsername("trainer.one");

        Trainer trainer2 = new Trainer();
        trainer2.setId(11L);
        trainer2.setUsername("trainer.two");

        trainee.setUsername("alan.walker");
        trainee.setTrainers(new HashSet<>());

        doNothing().when(authenticationService).authenticate("alan.walker", "pass123");
        when(traineeDao.findByUsername("alan.walker")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsernames(Set.of("trainer.one", "trainer.two")))
                .thenReturn(Set.of(trainer1, trainer2));
        when(traineeDao.update(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Set<Trainer> result = traineeService.updateTraineeTrainers(
                "alan.walker",
                "pass123",
                Set.of("trainer.one", "trainer.two")
        );

        assertEquals(2, result.size());
        assertTrue(result.contains(trainer1));
        assertTrue(result.contains(trainer2));

        ArgumentCaptor<Trainee> captor = ArgumentCaptor.forClass(Trainee.class);
        verify(traineeDao).update(captor.capture());
        assertEquals(2, captor.getValue().getTrainers().size());
    }

    @Test
    void setActive_shouldUpdateActiveFlag() {
        trainee.setUsername("alan.walker");
        trainee.setPassword("oldPass123");

        doNothing().when(authenticationService).authenticate("alan.walker", "oldPass123");
        when(traineeDao.findByUsername("alan.walker")).thenReturn(Optional.of(trainee));
        when(traineeDao.update(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        traineeService.setActive("alan.walker", "oldPass123", false);

        assertFalse(trainee.isActive());
        verify(traineeDao, times(1)).update(trainee);
    }

    @Test
    void changePassword_shouldUpdatePassword() {
        trainee.setUsername("alan.walker");
        trainee.setPassword("oldPass123");

        doNothing().when(authenticationService).authenticate("alan.walker", "oldPass123");
        when(traineeDao.findByUsername("alan.walker")).thenReturn(Optional.of(trainee));
        when(traineeDao.update(any(Trainee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        traineeService.changePassword("alan.walker", "oldPass123", "newPass456");

        assertEquals("newPass456", trainee.getPassword());
        verify(traineeDao, times(1)).update(trainee);
    }
}