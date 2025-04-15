package ru.yandex.practicum.filmorate.storage.genreStorage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAllGenre();

    Genre getGenre(int id);
}
