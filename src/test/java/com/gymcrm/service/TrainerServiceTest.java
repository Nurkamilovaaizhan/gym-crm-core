package com.gymcrm.service;

import com.gymcrm.dao.TrainerDao;
import com.gymcrm.dao.UserDao;
import com.gymcrm.model.Trainer;
import com.gymcrm.model.TrainingType;
import com.gymcrm.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
    private UserDao userDao;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void testCreateTrainer_ShouldGenerateCredentials() {
        User abstractUserMock = new User() { };
        abstractUserMock.setFirstName("Max");
        abstractUserMock.setLastName("Verstappen");

        TrainingType type = new TrainingType();
        type.setTrainingTypeName("F1-Fitness");

        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setUser(abstractUserMock);
        trainer.setSpecialization(type);

        when(userDao.findAllUsernames()).thenReturn(new HashSet<>());
        when(trainerDao.save(any(Trainer.class))).thenReturn(trainer);

        Trainer created = trainerService.createTrainer(trainer);

        assertNotNull(created);
        assertEquals("Max.Verstappen", created.getUser().getUsername());
        verify(trainerDao, times(1)).save(trainer);
    }

    @Test
    void testGetTrainerByUsername_ShouldReturnTrainer() {
        Trainer trainer = new Trainer();

        doNothing().when(authenticationService).authenticate("max.v", "pass");
        when(trainerDao.findByUsername("max.v")).thenReturn(Optional.of(trainer));

        Optional<Trainer> found = trainerService.getTrainerByUsername("max.v", "pass");

        assertTrue(found.isPresent());
        verify(authenticationService, times(1)).authenticate("max.v", "pass");
        verify(trainerDao, times(1)).findByUsername("max.v");
    }
}