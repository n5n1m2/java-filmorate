package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) {
        log.info("Вызван метод addLike");
        log.debug("Попытка добавить лайк от пользователя {} фильму {}", userId, filmId);
        filmStorage.getFilm(filmId).addLike(userStorage.getUser(userId));
    }

    public void removeLike(int filmId, int userId) {
        log.info("Вызван метод removeLike");
        log.debug("Попытка удалить лайк от пользователя {}", userId);
        filmStorage.getFilm(filmId).removeLike(userStorage.getUser(userId));
    }

    public List<Film> getMostPopularFilms(Integer count) {
        log.info("Вызван метод getMostPopularFilms");
        List<Film> filmArrayList = new ArrayList<>(filmStorage.getAllFilms());
        filmArrayList.sort(Comparator.comparingInt(film -> -film.getLikes().size()));
        int size = (count == null) ? 10 : count;
        return filmArrayList.subList(0, Math.min(size, filmArrayList.size()));
    }
}
