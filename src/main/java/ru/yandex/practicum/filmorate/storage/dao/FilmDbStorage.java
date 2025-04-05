package ru.yandex.practicum.filmorate.storage.dao;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.Like;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.storage.dao.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.storage.dao.mappers.LikesMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class FilmDbStorage implements FilmStorage {
    private final static String INSERT_FILM = "INSERT INTO films VALUES (?,?,?,?,?,?)";
    private final static String SELECT_FILM_BY_ID = "SELECT * FROM films WHERE film_id = ?";
    private final static String SELECT_ALL_FILMS = "SELECT * FROM films";
    private final static String UPDATE_FILM = "UPDATE films SET" +
            " film_id = ?," +
            " film_name = ?," +
            " description = ?," +
            " release = ?," +
            " duration = ?," +
            " rating = ?" +
            " WHERE film_id = ?";
    private final static String ADD_LIKE = "MERGE INTO likes (film_id, user_id) KEY (FILM_ID, USER_ID) VALUES (?,?)";
    private final static String GET_LIKE = "SELECT * FROM likes WHERE film_id = ?";
    private final static String ADD_GENRE = "INSERT INTO genre_names (genre_name) VALUES (?)";
    private final static String GET_GENRE = "SELECT * FROM genre AS g " +
            "LEFT JOIN genre_names AS n ON g.GENRE_ID = n.GENRE_ID " +
            "WHERE film_id = ?";
    private final static String SET_GENRE = "INSERT INTO genre_names (genre_id, genre_name) VALUES (?, ?)";
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Film> addFilm(Film film) {
        jdbcTemplate.update(INSERT_FILM,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating());
        addLikes(film);
        addGenre(film);
        try {
            Film film1 = jdbcTemplate.queryForObject(SELECT_FILM_BY_ID, new FilmRowMapper(), film.getId());
            return Optional.ofNullable(film1);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Film> updateFilm(Film film) {
        int rowsCount = jdbcTemplate.update(UPDATE_FILM,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating(),
                film.getId());
        if (rowsCount > 0) {
            return getFilm(film.getId());
        } else {
            throw new NotFoundException("Объект для обновления не найден");
        }
    }

    @Override
    public Optional<List<Film>> getAllFilms() {
        return Optional.of(jdbcTemplate.query(SELECT_ALL_FILMS, new FilmRowMapper()));
    }

    @Override
    public Optional<Film> getFilm(int id) {
        try {
            Film film = jdbcTemplate.queryForObject(SELECT_FILM_BY_ID, new FilmRowMapper(), id);
            return Optional.ofNullable(film);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private Optional<List<Like>> addLikes(Film film) {
        jdbcTemplate.batchUpdate(ADD_LIKE, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                List<Like> likes = film.getLikes().stream().toList();
                Like like = likes.get(i);
                ps.setInt(1, like.getFilmId());
                ps.setInt(2, like.getUserId());
            }

            @Override
            public int getBatchSize() {
                return film.getLikes() != null ? film.getLikes().size() : 0;
            }
        });
        return getLikes(film);
    }

    private Optional<List<Like>> getLikes(Film film) {
        return Optional.of(jdbcTemplate.query(GET_LIKE, new LikesMapper(), film.getId()));
    }

    private Optional<List<FilmGenre>> addGenre(Film film) {
        jdbcTemplate.batchUpdate(ADD_GENRE, new BatchPreparedStatementSetter() {

            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                List<FilmGenre> genres = film.getGenres().stream().toList();
                FilmGenre filmGenre = genres.get(i);
                ps.setInt(1, filmGenre.getId());
                ps.setInt(2, film.getId());
            }

            @Override
            public int getBatchSize() {
                return film.getGenres() != null ? film.getGenres().size() : 0;
            }
        });
        return getGenres(film);
    }

    private Optional<List<FilmGenre>> getGenres(Film film) {
        return Optional.of(jdbcTemplate.query(GET_GENRE, new GenreMapper(), film.getId()));
    }

    @PostConstruct
    private void init() {
        try {
            for (FilmGenre genre : FilmGenre.values()) {
                jdbcTemplate.update(SET_GENRE, genre.getId(), genre.name());
            }
        } catch (DataAccessException e) {
            log.error(e.getMessage());
            System.exit(1);
        }
    }
}
