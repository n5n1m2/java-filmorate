package ru.yandex.practicum.filmorate.storage.filmStorage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Optional<Film> addFilm(Film film);

    Optional<Film> updateFilm(Film film);

    Optional<List<Film>> getAllFilms();

    Optional<Film> getFilm(int id);

    void addLike(Film film, User user);

    void removeLike(Film film, User user);
}
