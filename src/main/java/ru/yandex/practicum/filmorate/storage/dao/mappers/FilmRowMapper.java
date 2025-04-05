package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<Film> {
    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release").toLocalDate(),
                rs.getInt("duration"),
                Rating.fromString(rs.getString("rating")),
                rs.getInt("film_id")
        );
    }
}
