package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Optional<List<Film>> getAllFilms();

    Optional<Film> getFilm(int id);
}
