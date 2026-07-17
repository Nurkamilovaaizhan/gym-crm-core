package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.dao.UserDao;
import com.gymcrm.model.Trainer;
import com.gymcrm.model.TrainingType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserDao userDao;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TrainerService trainerService;

    private TrainingType specialization;

    @BeforeEach
    void setUp() {
        specialization = new TrainingType();
        specialization.setId(1L);
        specialization.setTrainingTypeName("F1-Fitness");
    }

    @Test
    void createTrainer_shouldGenerateUsernameAndPassword() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Max");
        trainer.setLastName("Verstappen");
        trainer.setSpecialization(specialization);

        when(userDao.findAllUsernames()).thenReturn(new HashSet<>());
        when(trainerDao.save(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainer created = trainerService.createTrainer(trainer);

        assertNotNull(created);
        assertEquals("Max.Verstappen", created.getUsername());
        assertNotNull(created.getPassword());
        assertEquals(10, created.getPassword().length());
        assertEquals("Max", created.getFirstName());
        assertEquals("Verstappen", created.getLastName());
        assertEquals(specialization, created.getSpecialization());

        verify(trainerDao, times(1)).save(any(Trainer.class));
    }

    @Test
    void getTrainerByUsername_shouldAuthenticateAndReturnTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUsername("max.verstappen");

        doNothing().when(authenticationService).authenticate("max.verstappen", "pass123");
        when(trainerDao.findByUsername("max.verstappen")).thenReturn(Optional.of(trainer));

        Optional<Trainer> found = trainerService.getTrainerByUsername("max.verstappen", "pass123");

        assertTrue(found.isPresent());
        assertEquals("max.verstappen", found.get().getUsername());
        verify(authenticationService, times(1)).authenticate("max.verstappen", "pass123");
        verify(trainerDao, times(1)).findByUsername("max.verstappen");
    }

    @Test
    void updateTrainer_shouldUpdateFields() {
        Trainer existing = new Trainer();
        existing.setId(1L);
        existing.setFirstName("Old");
        existing.setLastName("Name");
        existing.setActive(true);
        existing.setSpecialization(specialization);

        Trainer updated = new Trainer();
        updated.setId(1L);
        updated.setFirstName("New");
        updated.setLastName("Surname");
        updated.setActive(false);
        updated.setSpecialization(specialization);

        doNothing().when(authenticationService).authenticate("max.verstappen", "pass123");
        when(trainerDao.findById(1L)).thenReturn(Optional.of(existing));
        when(trainerDao.update(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Trainer result = trainerService.updateTrainer("max.verstappen", "pass123", updated);

        assertEquals("New", result.getFirstName());
        assertEquals("Surname", result.getLastName());
        assertFalse(result.isActive());
        assertEquals(specialization, result.getSpecialization());

        ArgumentCaptor<Trainer> captor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerDao).update(captor.capture());
        assertEquals("New", captor.getValue().getFirstName());
    }

    @Test
    void changePassword_shouldUpdatePassword() {
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUsername("max.verstappen");
        trainer.setPassword("oldPass123");
        trainer.setFirstName("Max");
        trainer.setLastName("Verstappen");
        trainer.setActive(true);
        trainer.setSpecialization(specialization);

        doNothing().when(authenticationService).authenticate("max.verstappen", "oldPass123");
        when(trainerDao.findByUsername("max.verstappen")).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        trainerService.changePassword("max.verstappen", "oldPass123", "newPass456");

        assertEquals("newPass456", trainer.getPassword());
        verify(trainerDao, times(1)).update(trainer);
    }

    @Test
    void setActive_shouldUpdateActiveFlag() {
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUsername("max.verstappen");
        trainer.setPassword("oldPass123");
        trainer.setFirstName("Max");
        trainer.setLastName("Verstappen");
        trainer.setActive(true);
        trainer.setSpecialization(specialization);

        doNothing().when(authenticationService).authenticate("max.verstappen", "oldPass123");
        when(trainerDao.findByUsername("max.verstappen")).thenReturn(Optional.of(trainer));
        when(trainerDao.update(any(Trainer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        trainerService.setActive("max.verstappen", "oldPass123", false);

        assertFalse(trainer.isActive());
        verify(trainerDao, times(1)).update(trainer);
    }
}