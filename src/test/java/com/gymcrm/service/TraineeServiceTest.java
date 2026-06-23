package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {

    @Mock
    private TraineeDao traineeDao;

    @Mock
    private TrainerDao trainerDao;

    @InjectMocks
    private TraineeService traineeService;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = new Trainee();
        trainee.setId(1L);
        trainee.setFirstName("Alan");
        trainee.setLastName("Walker");
    }

    @Test
    void testCreateTrainee_ShouldGenerateCredentials() {
        // Симулируем пустые хранилища, чтобы не было дубликатов
        when(traineeDao.getStorageMap()).thenReturn(new HashMap<>());
        when(trainerDao.getStorageMap()).thenReturn(new HashMap<>());
        when(traineeDao.save(any(Trainee.class))).thenReturn(trainee);

        Trainee created = traineeService.createTrainee(trainee);

        assertNotNull(created);
        assertEquals("Alan.Walker", created.getUsername());
        assertNotNull(created.getPassword());
        assertEquals(10, created.getPassword().length());
        verify(traineeDao, times(1)).save(trainee);
    }

    @Test
    void testGetTrainee_ShouldReturnTrainee() {
        when(traineeDao.findById(1L)).thenReturn(Optional.of(trainee));

        Optional<Trainee> found = traineeService.getTrainee(1L);

        assertTrue(found.isPresent());
        assertEquals("Alan", found.get().getFirstName());
    }

    @Test
    void testDeleteTrainee_ShouldInvokeDelete() {
        doNothing().when(traineeDao).deleteById(1L);

        traineeService.deleteTrainee(1L);

        verify(traineeDao, times(1)).deleteById(1L);
    }
}