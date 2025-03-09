package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Set;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        log.info("Вызван метод addFriend");
        log.debug("Попытка добавить в друзья {} и {}", user, friend);
        user.addFriend(friend);
        friend.addFriend(user);
    }

    public void removeFriend(int userId, int friendId) {
        log.info("Вызван метод removeFriend");
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        log.debug("Попытка удалить из друзей {} и {}", user, friend);
        user.removeFriend(friend);
        friend.removeFriend(user);
    }

    public Set<User> getAllFriends(int userId) {
        log.info("Вызван метод getAllFriends");
        return userStorage.getUser(userId).getFriends();
    }
}
