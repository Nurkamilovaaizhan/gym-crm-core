package com.gymcrm.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "training")
@Getter
@Setter
@ToString
public class Training {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainee_id", nullable = false)
    private Trainee trainee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "training_type_id", nullable = false)
    private TrainingType trainingType;

    @Column(nullable = false)
    private String trainingName;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date trainingDate;

    @Column(nullable = false)
    private int trainingDuration;
}