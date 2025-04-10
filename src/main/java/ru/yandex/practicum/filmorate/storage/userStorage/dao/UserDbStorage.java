package ru.yandex.practicum.filmorate.storage.userStorage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.FriendRequest;
import ru.yandex.practicum.filmorate.model.RequestStatus;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.RequestStatusMapper;
import ru.yandex.practicum.filmorate.storage.mappers.UserMapper;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<User> createUser(User user) {
        String addUser = "INSERT INTO users (" +
                "USER_ID, " +
                "login, " +
                "user_name, " +
                "birthday, " +
                "email) VALUES (DEFAULT,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(addUser, new String[]{"user_id"});
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getName());
            ps.setDate(3, Date.valueOf(user.getBirthday()));
            ps.setString(4, user.getEmail());
            return ps;
        }, keyHolder);
        log.debug("User id: {}", keyHolder.getKey());
        return getUser(keyHolder.getKey().intValue());
    }

    @Override
    public Optional<User> updateUser(User user) {
        String updateUser = "UPDATE users SET " +
                "user_id = ?," +
                "login = ?," +
                "user_name = ?," +
                "birthday = ?," +
                "email = ?" +
                "WHERE user_id = ?";

        jdbcTemplate.update(updateUser,
                user.getId(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getEmail(), user.getId());
        return getUser(user.getId());
    }

    @Override
    public Optional<List<User>> getAllUsers() {
        String getAllUsers = "SELECT * FROM users";
        return Optional.of(jdbcTemplate.query(getAllUsers, new UserMapper()));
    }

    @Override
    public Optional<User> getUser(int id) {
        String getUser = "SELECT * FROM users WHERE user_id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(getUser, new UserMapper(), id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addFriend(User user, User friend) {
        String addFriend = "INSERT INTO friends (user_id, friend_id, request_status) VALUES (?,?,?)";
        Optional<FriendRequest> request = getFriend(friend.getId(), user.getId());
        if (request.isPresent()) {
            removeFriendOrDeclineRequest(user, friend);
            jdbcTemplate.update(addFriend, user.getId(), friend.getId(), RequestStatus.ACCEPTED.name().toLowerCase());
            jdbcTemplate.update(addFriend, friend.getId(), user.getId(), RequestStatus.ACCEPTED.name().toLowerCase());
        } else {
            jdbcTemplate.update(addFriend, user.getId(), friend.getId(), RequestStatus.PENDING.name().toLowerCase());
        }
    }

    @Override
    public void removeFriendOrDeclineRequest(User user, User friend) {
        String removeFriend = "DELETE FROM friends WHERE user_id=? AND friend_id=?";
        jdbcTemplate.update(removeFriend, user.getId(), friend.getId());
    }

    private Optional<FriendRequest> getFriend(int user_id, int friend_id) {
        String getFriend = "SELECT * FROM friends WHERE user_id=? AND friend_id=?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(getFriend, new RequestStatusMapper(), user_id, friend_id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> getAllFriends(int user_id) {
        String getAllFriends = "SELECT friend_id FROM friends WHERE user_id=?";
        String getUsersWhereId = "SELECT * FROM users WHERE user_id IN (:id)";
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        getUser(user_id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        try {
            List<Integer> friends_id = jdbcTemplate.queryForList(getAllFriends, Integer.class, user_id);
            return namedParameterJdbcTemplate.query(getUsersWhereId,
                    new MapSqlParameterSource("id", friends_id),
                    new UserMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }
}
