package com.gymcrm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Date;

@Getter
@Setter
@ToString(callSuper = true)
public class Trainee extends User {
    private Long id;
    private Date dateOfBirth;
    private String address;
}