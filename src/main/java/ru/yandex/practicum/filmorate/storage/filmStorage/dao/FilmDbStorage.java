package ru.yandex.practicum.filmorate.storage.filmStorage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.filmStorage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.mappers.FilmRowMapper;

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
        List<Film> films = new ArrayList<>(jdbcTemplate.query(selectAllFilms, new FilmRowMapper(jdbcTemplate)));
        return Optional.of(films);
    }

    @Override
    public Optional<Film> getFilm(int id) {
        String selectFilmById = "SELECT * FROM films AS f " +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
                "WHERE film_id = ?";
        try {
            Film film = jdbcTemplate.queryForObject(selectFilmById, new FilmRowMapper(jdbcTemplate), id);
            return Optional.ofNullable(film);
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

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String selectPopularFilms = "SELECT f.*, r.RATING_NAME, count(l.USER_ID) likes_count " +
                "FROM films AS f " +
                "JOIN likes AS l ON f.film_id = l.film_id " +
                "LEFT JOIN rating AS r ON f.rating_id = r.rating_id " +
                "GROUP BY f.FILM_ID, r.RATING_NAME  " +
                "ORDER BY likes_count DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(selectPopularFilms, new FilmRowMapper(jdbcTemplate), count == null ? 10 : count);
    }


    private void addGenre(Film film, int filmId) {
        String sql = "MERGE INTO genre (genre_id, film_id) KEY (GENRE_ID, FILM_ID) VALUES (?, ?)";
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
}
