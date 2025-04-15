package ru.yandex.practicum.filmorate.storage.userStorage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    Optional<User> createUser(User user);

    Optional<User> updateUser(User user);

    Optional<List<User>> getAllUsers();

    Optional<User> getUser(int id);

    void addFriend(User user, User friend);

    void removeFriendOrDeclineRequest(User user, User friend);

    List<User> getAllFriends(int userId);
}
