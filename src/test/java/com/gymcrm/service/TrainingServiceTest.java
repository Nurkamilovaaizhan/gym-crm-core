package com.gymcrm.service;

import com.gymcrm.dao.TrainingDao;
import com.gymcrm.model.Training;
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

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void testCreateTraining_ShouldSave() {
        Training training = new Training();
        training.setTrainingName("Yoga");
        when(trainingDao.save(training)).thenReturn(training);

        Training created = trainingService.createTraining(training);

        assertNotNull(created);
        assertEquals("Yoga", created.getTrainingName());
        verify(trainingDao, times(1)).save(training);
    }
}