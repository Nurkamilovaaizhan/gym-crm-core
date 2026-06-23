package com.gymcrm.service;

import com.gymcrm.dao.TraineeDao;
import com.gymcrm.dao.TrainerDao;
import com.gymcrm.model.Trainer;
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
class TrainerServiceTest {

    @Mock
    private TrainerDao trainerDao;

    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void testCreateTrainer_ShouldGenerateCredentials() {
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        trainer.setFirstName("Max");
        trainer.setLastName("Verstappen");

        when(traineeDao.getStorageMap()).thenReturn(new HashMap<>());
        when(trainerDao.getStorageMap()).thenReturn(new HashMap<>());
        when(trainerDao.save(any(Trainer.class))).thenReturn(trainer);

        Trainer created = trainerService.createTrainer(trainer);

        assertEquals("Max.Verstappen", created.getUsername());
        verify(trainerDao, times(1)).save(trainer);
    }

    @Test
    void testGetTrainer_ShouldReturnTrainer() {
        Trainer trainer = new Trainer();
        when(trainerDao.findById(2L)).thenReturn(Optional.of(trainer));

        Optional<Trainer> found = trainerService.getTrainer(2L);

        assertTrue(found.isPresent());
    }
}