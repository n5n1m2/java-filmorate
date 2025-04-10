package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.FriendRequest;
import ru.yandex.practicum.filmorate.model.RequestStatus;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RequestStatusMapper implements RowMapper<FriendRequest> {

    @Override
    public FriendRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new FriendRequest(
                rs.getInt("user_id"),
                rs.getInt("friend_id"),
                RequestStatus.valueOf(rs.getString("request_status"))
        );
    }
}
