package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;

@AllArgsConstructor
public enum Rating {
    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC17("NC-17");
    private final String rating;

    public static Rating fromString(String rating) {
        for (Rating r : Rating.values()) {
            if (r.rating.equalsIgnoreCase(rating)) {
                return r;
            }
        }
        throw new NotFoundException("не найден нужный жанр");
    }

    @Override
    public String toString() {
        return rating;
    }
}
