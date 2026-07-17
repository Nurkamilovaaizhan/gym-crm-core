package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.dao.TrainingDao;
import com.gymcrm.model.Trainee;
import com.gymcrm.model.Trainer;
import com.gymcrm.model.Training;
import com.gymcrm.model.TrainingType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDao trainingDao;

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void addTraining_shouldSaveAndConnectEntities() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        trainee.setUsername("alan.walker");
        trainee.setTrainers(new HashSet<>());

        TrainingType type = new TrainingType();
        type.setId(5L);
        type.setTrainingTypeName("Yoga");

        Trainer trainer = new Trainer();
        trainer.setId(2L);
        trainer.setUsername("max.verstappen");
        trainer.setSpecialization(type);

        Training training = new Training();
        training.setTrainingName("Morning Yoga");
        training.setTrainingDate(new Date());
        training.setTrainingDuration(60);

        doNothing().when(authenticationService).authenticate("admin", "pass");
        when(traineeDao.findByUsername("alan.walker")).thenReturn(Optional.of(trainee));
        when(trainerDao.findByUsername("max.verstappen")).thenReturn(Optional.of(trainer));
        when(trainingDao.save(any(Training.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Training created = trainingService.addTraining(
                "admin",
                "pass",
                "alan.walker",
                "max.verstappen",
                training
        );

        assertNotNull(created);
        assertEquals("Morning Yoga", created.getTrainingName());
        assertEquals(type, created.getTrainingType());
        assertEquals(trainee, created.getTrainee());
        assertEquals(trainer, created.getTrainer());
        assertTrue(trainee.getTrainers().contains(trainer));

        ArgumentCaptor<Training> captor = ArgumentCaptor.forClass(Training.class);
        verify(trainingDao).save(captor.capture());
        assertEquals("Morning Yoga", captor.getValue().getTrainingName());
    }

    @Test
    void getTraineeTrainings_shouldAuthenticateAndQuery() {
        doNothing().when(authenticationService).authenticate("alan.walker", "pass");
        when(trainingDao.findByTraineeCriteria(eq("alan.walker"), any(), any(), any(), any()))
                .thenReturn(java.util.List.of());

        var result = trainingService.getTraineeTrainings("alan.walker", "pass", null, null, null, null);

        assertNotNull(result);
        verify(authenticationService, times(1)).authenticate("alan.walker", "pass");
        verify(trainingDao, times(1)).findByTraineeCriteria("alan.walker", null, null, null, null);
    }

    @Test
    void getTrainerTrainings_shouldAuthenticateAndQuery() {
        doNothing().when(authenticationService).authenticate("max.verstappen", "pass");
        when(trainingDao.findByTrainerCriteria(eq("max.verstappen"), any(), any(), any()))
                .thenReturn(java.util.List.of());

        var result = trainingService.getTrainerTrainings("max.verstappen", "pass", null, null, null);

        assertNotNull(result);
        verify(authenticationService, times(1)).authenticate("max.verstappen", "pass");
        verify(trainingDao, times(1)).findByTrainerCriteria("max.verstappen", null, null, null);
    }
}