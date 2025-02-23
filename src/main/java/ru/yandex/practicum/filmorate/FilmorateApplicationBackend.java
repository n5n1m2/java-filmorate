package ru.yandex.practicum.filmorate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.IdGenerationException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class FilmorateApplicationBackend {
    private Map<Integer, Film> films = new HashMap<>();
    private Map<Integer, User> users = new HashMap<>();

    public Film addFilm(Film film) {
        log.info("Вызван метод addFilm");
        log.debug("Получен объект " + film);

        log.debug("Попытка добавить " + film);
        films.put(film.getId(), film);
        log.debug("Добавлено " + films.get(film.getId()));
        return films.get(film.getId());
    }

    public User addUser(User user) {
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

    public Film updateFilm(Film film) {
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

    public User updateUser(User user) {
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

    public Map<Integer, Film> getAllFilms() {
        log.info("Вызван метод getAllFilms");
        log.debug("Будет возвращено  " + films);

        return films;
    }

    public Map<Integer, User> getAllUsers() {
        log.info("Вызван метод getAllUsers");
        log.debug("Будет возвращено  " + users);

        return users;
    }

    public Integer getNextId(Class<?> clazz) {
        log.info("Вызван метод getNextId");
        log.debug("В метод передана сущность " + clazz);

        int id;
        Map<Integer, ?> map;
        if (clazz.equals(FilmController.class)) {
            map = films;
        } else if (clazz.equals(UserController.class)) {
            map = users;
        } else {
            throw new IdGenerationException("Не распознана сущность для генерации id. Передано: " + clazz.getName());
        }
        id = map
                .keySet()
                .stream()
                .mapToInt(obj -> obj)
                .max()
                .orElse(0) + 1;
        log.debug("Метод вернул " + id);
        return id;
    }
}
