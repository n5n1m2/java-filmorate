package ru.yandex.practicum.filmorate.service.user;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.userStorage.UserStorage;

import java.util.List;

@Slf4j
@Service
@Validated
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(final int userId, final int friendId) {
        log.info("Вызван метод addFriend");
        User user = userStorage.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User friend = userStorage.getUser(friendId).orElseThrow(() -> new NotFoundException("Друг не найден"));
        log.debug("Попытка добавить в друзья {} и {}", user, friend);
        userStorage.addFriend(user, friend);
    }

    public void removeFriend(final int userId, final int friendId) {
        log.info("Вызван метод removeFriend");
        User user = userStorage.getUser(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        User friend = userStorage.getUser(friendId).orElseThrow(() -> new NotFoundException("Друг не найден"));
        log.debug("Попытка удалить из друзей {} и {}", user, friend);
        userStorage.removeFriendOrDeclineRequest(user, friend);
    }

    public List<User> getAllFriends(final int userId) {
        log.info("Вызван метод getAllFriends");
        return userStorage.getAllFriends(userId);
    }

    public User createUser(@Valid final User user) {
        return userStorage.createUser(user).orElseThrow(() -> new NotFoundException("Пользователь не был добавлен"));
    }

    public User updateUser(@Valid final User user) {
        return userStorage.updateUser(user).orElseThrow(() -> new NotFoundException("Пользователь не был обновлен"));
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers().orElseThrow(() -> new NotFoundException("Массив не передан"));
    }

    public User getUser(final int id) {
        return userStorage.getUser(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
    }
}
