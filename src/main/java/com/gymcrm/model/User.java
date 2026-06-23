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
    @ToString.Exclude // Исключаем пароль из логов ради безопасности
    private String password;
    private boolean isActive;
}