package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.FilmorateApplicationBackend;
import ru.yandex.practicum.filmorate.interfaces.Update;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;

@RestController
@RequestMapping("/users")
public class UserController {
    private final FilmorateApplicationBackend filmApplication;

    @Autowired
    public UserController(FilmorateApplicationBackend filmorateApplicationBackend) {
        this.filmApplication = filmorateApplicationBackend;
    }

    @PostMapping
    private User createUser(@Validated @RequestBody User user) {
        user.setId(filmApplication.getNextId(UserController.class));
        return filmApplication.addUser(user);
    }

    @PutMapping
    private User updateUser(@Validated({Update.class, Default.class}) @RequestBody User user) {
        return filmApplication.updateUser(user);
    }

    @GetMapping
    private ArrayList<User> getAllUsers() {
        return new ArrayList<>(filmApplication.getAllUsers().values());
    }
}