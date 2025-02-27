package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.Update;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    private User createUser(@Validated @RequestBody User user) {
        user.setId(getNextId());
        log.info("Вызван метод addUser");
        log.debug("Получен объект " + user);

        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.debug("Имя заменено на  " + user.getLogin());
        }
        log.debug("Попытка добавить " + user);
        users.put(user.getId(), user);
        log.debug("Добавлено " + users.get(user.getId()));
        return users.get(user.getId());
    }

    @PutMapping
    private User updateUser(@Validated({Update.class, Default.class}) @RequestBody User user) {
        log.info("Вызван метод updateUser");
        log.debug("Получен объект " + user);
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Переданный пользователь ранее не добавлялся");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.debug("Имя заменено на  " + user.getLogin());
        }
        log.debug("Попытка заменить {} на {}", users.get(user.getId()), user);
        users.replace(user.getId(), user);
        log.debug("В map добавлено " + users.get(user.getId()));
        return users.get(user.getId());
    }

    @GetMapping
    private ArrayList<User> getAllUsers() {
        log.info("Вызван метод getAllUsers");
        log.debug("Будет возвращено  " + users);
        return new ArrayList<>(users.values());
    }

    private Integer getNextId() {
        log.info("Вызван метод getNextId");
        int id = users
                .keySet()
                .stream()
                .mapToInt(obj -> obj)
                .max()
                .orElse(0) + 1;
        log.debug("Метод вернул " + id);
        return id;
    }
}