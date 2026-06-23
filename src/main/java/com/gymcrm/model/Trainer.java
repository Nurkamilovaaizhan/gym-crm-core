package com.gymcrm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Trainer extends User {
    private Long id;
    private String specialization;
}