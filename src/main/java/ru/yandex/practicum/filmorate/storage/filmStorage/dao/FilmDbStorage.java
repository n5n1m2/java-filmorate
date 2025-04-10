package ru.yandex.practicum.filmorate.storage.filmStorage.dao;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.filmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.mappers.LikesMapper;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initRating() {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM rating", Integer.class);
        if (count == null || count != 0) {
            return;
        }
        String sql = "INSERT INTO rating (rating_name) VALUES (?)";
        for (Rating value : Rating.values()) {
            jdbcTemplate.update(sql, value.toString());
        }
    }

    @Override
    public Optional<Film> addFilm(Film film) {
        if (film.getMpa().getId() < 1 || film.getMpa().getId() > 5) {
            throw new NotFoundException("Mpa не найден");
        }
        String insertFilm = "INSERT INTO films (FILM_ID, " +
                "FILM_NAME, " +
                "DESCRIPTION, " +
                "RELEASE, " +
                "DURATION, " +
                "RATING_ID) " +
                "VALUES (DEFAULT,?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertFilm, new String[]{"FILM_ID"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            if (film.getMpa() != null) {
                ps.setInt(5, film.getMpa().getId());
            } else {
                ps.setNull(5, Types.INTEGER);
            }
            return ps;
        }, keyHolder);
        addGenre(film, keyHolder.getKey().intValue());
        return getFilm(keyHolder.getKey().intValue());
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        String updateFilm = "UPDATE films SET" +
                " film_id = ?," +
                " film_name = ?," +
                " description = ?," +
                " release = ?," +
                " duration = ?," +
                " RATING_ID = ?" +
                " WHERE film_id = ?";

        int rowsCount = jdbcTemplate.update(updateFilm,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        removeGenre(film);
        addGenre(film, film.getId());
        if (rowsCount > 0) {
            return getFilm(film.getId());
        } else {
            throw new NotFoundException("Объект для обновления не найден");
        }
    }

    @Override
    public Optional<List<Film>> getAllFilms() {
        String selectAllFilms = "SELECT * FROM films AS f " +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id";
        List<Film> films = new ArrayList<>();
        for (Film film : jdbcTemplate.query(selectAllFilms, new FilmRowMapper(jdbcTemplate))) {
            films.add(setLikes(film));
        }
        return Optional.of(films);
    }

    @Override
    public Optional<Film> getFilm(int id) {
        String selectFilmById = "SELECT * FROM films AS f " +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
                "WHERE film_id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(selectFilmById, new FilmRowMapper(jdbcTemplate), id);
            return Optional.ofNullable(setLikes(film));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addLike(Film film, User user) {
        String addLike = "MERGE INTO likes (film_id, user_id) KEY (FILM_ID, USER_ID) VALUES (?,?)";
        jdbcTemplate.update(addLike, film.getId(), user.getId());
    }

    @Override
    public void removeLike(Film film, User user) {
        String removeLike = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(removeLike, film.getId(), user.getId());
    }


    private void addGenre(Film film, int filmId) {
        String sql = "INSERT INTO genre (genre_id, film_id) VALUES (?, ?)";
        Set<Genre> genres = film.getGenres();
        if (genres == null || genres.isEmpty()) {
            return;
        }
        try {
            jdbcTemplate.batchUpdate(sql, genres, genres.size(), (ps, genre) -> {
                ps.setInt(1, genre.getId());
                ps.setInt(2, filmId);
            });
        } catch (RuntimeException e) {
            throw new NotFoundException("Ошибка добавления рейтинга");
        }
    }

    private void removeGenre(Film film) {
        String removeGenre = "DELETE FROM genre WHERE film_id = ?";
        jdbcTemplate.update(removeGenre, film.getId());
    }

    private Film setLikes(Film film) {
        String setLike = "SELECT * FROM likes WHERE film_id = ?";
        try {
            List<Like> likes = jdbcTemplate.query(setLike, new LikesMapper(), film.getId());
            for (Like like : likes) {
                film.addLike(like);
            }
            return film;
        } catch (RuntimeException e) {
            return film;
        }
    }
}
