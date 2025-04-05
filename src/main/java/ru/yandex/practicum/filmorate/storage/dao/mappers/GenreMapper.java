package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FilmGenre;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<FilmGenre> {

    @Override
    public FilmGenre mapRow(ResultSet rs, int rowNum) throws SQLException {
        return FilmGenre.valueOf(rs.getString("genre_name"));
    }
}
