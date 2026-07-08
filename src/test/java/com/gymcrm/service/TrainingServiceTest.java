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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void testAddTraining_ShouldSaveAndEstablishRelations() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);

        TrainingType type = new TrainingType();
        type.setTrainingTypeName("Yoga");

        Trainer trainer = new Trainer();
        trainer.setId(2L);
        trainer.setSpecialization(type);

        Training training = new Training();
        training.setTrainingName("Morning Yoga");
        training.setTrainee(trainee);
        training.setTrainer(trainer);

        doNothing().when(authenticationService).authenticate("auth", "pass");
        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));
        when(trainerDao.findById(2L)).thenReturn(Optional.of(trainer));
        when(trainingDao.save(any(Training.class))).thenReturn(training);

        Training created = trainingService.addTraining("auth", "pass", training);

        assertNotNull(created);
        assertEquals("Morning Yoga", created.getTrainingName());
        assertEquals(type, created.getTrainingType());
        assertTrue(trainee.getTrainers().contains(trainer));
        verify(trainingDao, times(1)).save(any(Training.class));
    }
}