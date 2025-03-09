package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    @Override
    public Film addFilm(final Film film) {
        film.setId(getNextId());
        log.info("Вызван метод addFilm");
        log.debug("Получен объект " + film);
        log.debug("Попытка добавить " + film);
        films.put(film.getId(), film);
        log.debug("Добавлено " + films.get(film.getId()));
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(final Film film) {
        log.info("Вызван метод updateFilm");
        log.debug("Получен объект " + film);
        if (!films.containsKey(film.getId())) {
            throw new IllegalArgumentException("Переданный фильм ранее не добавлялся");
        }
        log.debug("Попытка заменить {} на {}", films.get(film.getId()), film);
        films.replace(film.getId(), film);
        log.debug("В map добавлено " + films.get(film.getId()));
        return films.get(film.getId());
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilm(int id) {
        if (!films.containsKey(id)) {
            throw new IllegalArgumentException("Переданный id фильма не найден: " + id);
        }
        return films.get(id);
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
