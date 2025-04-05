package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

public enum FilmGenre {
    COMEDY,
    DRAMA,
    CARTOON,
    THRILLER,
    DOCUMENTARY,
    ACTION;

    @Getter
    private final int id;

    FilmGenre() {
        this.id = ordinal() + 1;
    }
}
