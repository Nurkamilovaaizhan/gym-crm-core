package com.gymcrm.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class User {
    private String firstName;
    private String lastName;
    private String username;
    @ToString.Exclude // password must not appear in logs
    private String password;
    private boolean isActive;
}