package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

@AllArgsConstructor
public class FilmRowMapper implements RowMapper<Film> {
    private JdbcTemplate jdbcTemplate;

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        String sql = "SELECT * FROM genre AS g " +
                "LEFT JOIN genre_names AS gn ON g.genre_id = gn.genre_id " +
                "WHERE FILM_ID = ?";

        return new Film(
                rs.getString("film_name"),
                rs.getString("description"),
                rs.getDate("release").toLocalDate(),
                rs.getInt("duration"),
                new LinkedHashSet<>(jdbcTemplate.query(sql, new GenreMapper(), rs.getInt("film_id"))),
                new Mpa(rs.getInt("rating_id"), rs.getString("rating_name")),
                rs.getInt("film_id")
        );
    }
}
