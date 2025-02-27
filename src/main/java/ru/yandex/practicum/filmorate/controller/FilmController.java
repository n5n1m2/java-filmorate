package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.interfaces.Update;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    private Film addFilm(@Validated @RequestBody Film film) {
        film.setId(getNextId());
        log.info("Вызван метод addFilm");
        log.debug("Получен объект " + film);
        log.debug("Попытка добавить " + film);
        films.put(film.getId(), film);
        log.debug("Добавлено " + films.get(film.getId()));
        return films.get(film.getId());

    }

    @PutMapping
    private Film updateFilm(@Validated({Update.class, Default.class}) @RequestBody Film film) {
        log.info("Вызван метод updateFilm");
        log.debug("Получен объект " + film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Переданный фильм ранее не добавлялся");
        }
        log.debug("Попытка заменить {} на {}", films.get(film.getId()), film);
        films.replace(film.getId(), film);
        log.debug("В map добавлено " + films.get(film.getId()));
        return films.get(film.getId());
    }

    @GetMapping
    private ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    private Integer getNextId() {
        log.info("Вызван метод getNextId");
        int id = films
                .keySet()
                .stream()
                .mapToInt(obj -> obj)
                .max()
                .orElse(0) + 1;
        log.debug("Метод вернул " + id);
        return id;
    }
}