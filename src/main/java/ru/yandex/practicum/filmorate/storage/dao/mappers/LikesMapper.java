package ru.yandex.practicum.filmorate.storage.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Like;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LikesMapper implements RowMapper<Like> {

    @Override
    public Like mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Like(rs.getInt("film_id"),
                rs.getInt("user_id"),
                rs.getInt("like_id"));
    }
}
