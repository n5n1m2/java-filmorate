package ru.yandex.practicum.filmorate.storage.genreStorage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genreStorage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.mappers.GenreMapper;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getAllGenre() {
        String getGenre = "SELECT * FROM genre_names ORDER BY genre_id";
        return jdbcTemplate.query(getGenre, new GenreMapper());
    }

    @Override
    public Genre getGenre(int id) {
        String getGenre = "SELECT * FROM genre_names " +
                "WHERE genre_id = ?";
        try {
            return jdbcTemplate.queryForObject(getGenre, new GenreMapper(), id);
        } catch (RuntimeException e) {
            throw new NotFoundException("Запрашиваемый жанр не найден.");
        }

    }
}
