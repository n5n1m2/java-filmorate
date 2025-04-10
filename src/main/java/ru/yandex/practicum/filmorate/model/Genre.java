package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Value;


@Value
@EqualsAndHashCode(exclude = "name")
public class Genre implements Comparable<Genre> {

    Integer id;
    String name;

    public int compareTo(Genre genre) {
        return (genre.getId().compareTo(this.getId()));
    }
}
