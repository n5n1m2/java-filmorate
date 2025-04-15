package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.interfaces.Update;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    private User createUser(@Validated @RequestBody final User user) {
        return userService.createUser(user);
    }

    @PutMapping
    private User updateUser(@Validated({Update.class, Default.class}) @RequestBody final User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    private void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    private void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping
    private List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    private User getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @GetMapping("{id}/friends")
    private List<User> getFriends(@PathVariable int id) {
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    private ArrayList<User> getCommonFriend(@PathVariable int id, @PathVariable int otherId) {
        ArrayList<User> arrayList = new ArrayList<>(userService.getAllFriends(id));
        arrayList.retainAll(userService.getAllFriends(otherId));
        return arrayList;
    }
}