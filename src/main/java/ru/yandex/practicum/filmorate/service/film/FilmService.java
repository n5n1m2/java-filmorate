package ru.yandex.practicum.filmorate.service.film;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.filmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;

import java.util.List;

@Validated
@Slf4j
@AllArgsConstructor
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public void addLike(final int filmId, final int userId) {
        log.info("Вызван метод addLike");
        log.debug("Попытка добавить лайк от пользователя {} фильму {}", userId, filmId);
        filmStorage.addLike(
                getFilm(filmId),
                userStorage.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден")));
    }

    public void removeLike(final int filmId, final int userId) {
        log.info("Вызван метод removeLike");
        log.debug("Попытка удалить лайк от пользователя {}", userId);
        filmStorage.removeLike(
                getFilm(filmId),
                userStorage.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"))
        );
    }

    public List<Film> getMostPopularFilms(final Integer count) {
        log.info("Вызван метод getMostPopularFilms");
        return filmStorage.getPopularFilms(count);
    }

    public Film addFilm(@Valid final Film film) {
        return filmStorage.addFilm(film).orElseThrow(() -> new NotFoundException("Фильм не был добавлен"));
    }

    public Film updateFilm(@Valid final Film film) {
        return filmStorage.updateFilm(film).orElseThrow(() -> new NotFoundException("Фильм не обновлен"));
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms().orElseThrow(() -> new NotFoundException("Не передан массив"));
    }

    public Film getFilm(final int id) {
        return filmStorage.getFilm(id).orElseThrow(() -> new NotFoundException("Фильм не найден"));
    }


}
