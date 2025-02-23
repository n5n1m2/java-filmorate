package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.groups.Default;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.FilmorateApplicationBackend;
import ru.yandex.practicum.filmorate.interfaces.Update;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmorateApplicationBackend filmApplication;

    @Autowired
    public FilmController(FilmorateApplicationBackend filmApplication) {
        this.filmApplication = filmApplication;
    }

    @PostMapping
    private Film addFilm(@Validated @RequestBody Film film) {
        film.setId(filmApplication.getNextId(FilmController.class));
        return filmApplication.addFilm(film);
    }

    @PutMapping
    private Film updateFilm(@Validated({Update.class, Default.class}) @RequestBody Film film) {
        return filmApplication.updateFilm(film);

    }

    @GetMapping
    private ArrayList<Film> getAllFilms() {
        return new ArrayList<>(filmApplication.getAllFilms().values());
    }
}
