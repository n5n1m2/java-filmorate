package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.genre.GenreService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmService.class, GenreService.class})
@Sql(scripts = {"/schema.sql", "/data.sql"})
public class FilmServiceDbTests {
    private final FilmService filmService;
    private final GenreService genreService;

    @Test
    public void testAddFilm() {
        Film film = new Film(
                "Фильм тест",
                "Описание",
                LocalDate.of(1999, 12, 15),
                110,
                new LinkedHashSet<>(Arrays.asList(genreService.getGenre(2), genreService.getGenre(3))),
                new Mpa(4, "R"),
                null
        );
        Film film1 = filmService.addFilm(film);
        Assertions.assertEquals(film, film1);
        Assertions.assertEquals(film, filmService.getFilm(film1.getId()));
    }

    @Test
    public void testUpdateFilm() {
        Film film = filmService.getFilm(1);
        System.out.println(film);
        Film film2 = new Film(
                film.getName() + " Тест изменения",
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenres(),
                film.getMpa(),
                film.getId()
        );
        Film film3 = filmService.updateFilm(film2);
        Assertions.assertEquals(film2, film3);
        Assertions.assertEquals(film3, filmService.getFilm(film2.getId()));
    }

    @Test
    public void testUpdateUnknownFilm() {
        Film film0 = new Film(
                "Фильм тест",
                "Описание",
                LocalDate.of(1999, 12, 15),
                110,
                new LinkedHashSet<>(Arrays.asList(genreService.getGenre(2), genreService.getGenre(3))),
                new Mpa(4, "R"),
                null
        );
        Film film = filmService.addFilm(film0);

        Film film2 = new Film(
                film.getName() + " Тест изменения",
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenres(),
                film.getMpa(),
                film.getId() + 200
        );
        assertThrows(NotFoundException.class, () -> filmService.updateFilm(film2));

    }

    @Test
    public void testAddFilmFail() {
        assertThrows(ConstraintViolationException.class, () -> filmService.addFilm(new Film(
                "",
                "Описание",
                LocalDate.now(),
                120,
                new LinkedHashSet<>(),
                new Mpa(1, null),
                null
        )));
        assertThrows(ConstraintViolationException.class, () -> filmService.addFilm(new Film(
                "Фильм",
                "О".repeat(201),
                LocalDate.now(),
                120,
                new LinkedHashSet<>(),
                new Mpa(1, null),
                null
        )));
        assertThrows(ValidationException.class, () -> filmService.addFilm(new Film("Фильм",
                "Описание",
                LocalDate.of(1890, 1, 1),
                120,
                new LinkedHashSet<>(),
                new Mpa(1, null),
                null)));

        assertThrows(ValidationException.class, () -> filmService.addFilm(new Film(
                "Фильм",
                "Описание",
                LocalDate.now(),
                -10,
                new LinkedHashSet<>(),
                new Mpa(1, null),
                null)));

        assertThrows(NotFoundException.class, () -> filmService.addFilm(new Film(
                "Фильм",
                "Описание",
                LocalDate.now(),
                120,
                new LinkedHashSet<>(),
                new Mpa(6, null),
                null)));

        assertThrows(NotFoundException.class, () -> filmService.addFilm(new Film(
                "Фильм",
                "Описание",
                LocalDate.now(),
                120,
                new LinkedHashSet<>(Set.of(new Genre(10, null))),
                new Mpa(1, null),
                null)));
    }

    @Test
    public void testGetAllFilms() {
        assertFalse(filmService.getAllFilms().isEmpty());
    }
}
