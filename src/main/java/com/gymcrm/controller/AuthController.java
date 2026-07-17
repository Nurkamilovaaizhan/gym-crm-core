package com.gymcrm.controller;

import com.gymcrm.facade.GymFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final GymFacade gymFacade;

    public AuthController(GymFacade gymFacade) {
        this.gymFacade = gymFacade;
    }

    @GetMapping("/api/login")
    public void login(@RequestParam(value = "username") String username,
                      @RequestParam(value = "password") String password) {
        gymFacade.login(username, password);
    }

    @PutMapping("/api/login/password")
    public void changePassword(@RequestParam(value = "username") String username,
                               @RequestParam(value = "oldPassword") String oldPassword,
                               @RequestParam(value = "newPassword") String newPassword) {
        gymFacade.changePassword(username, oldPassword, newPassword);
    }
}