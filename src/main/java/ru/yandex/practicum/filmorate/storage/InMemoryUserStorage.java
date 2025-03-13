package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User createUser(final User user) {
        user.setId(getNextId());
        log.info("Вызван метод addUser");
        log.debug("Получен объект " + user);

        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.debug("Имя заменено на  " + user.getLogin());
        }
        log.debug("Попытка добавить " + user);
        users.put(user.getId(), user);
        log.debug("Добавлено " + users.get(user.getId()));
        return users.get(user.getId());
    }

    @Override
    public User updateUser(final User user) {
        log.info("Вызван метод updateUser");
        log.debug("Получен объект " + user);
        if (!users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Переданный пользователь ранее не добавлялся");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
            log.debug("Имя заменено на  " + user.getLogin());
        }
        log.debug("Попытка заменить {} на {}", users.get(user.getId()), user);
        users.replace(user.getId(), user);
        log.debug("В map добавлено " + users.get(user.getId()));
        return users.get(user.getId());
    }

    @Override
    public ArrayList<User> getAllUsers() {
        log.info("Вызван метод getAllUsers");
        log.debug("Будет возвращено  " + users);
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(int id) {
        if (!users.containsKey(id)) {
            throw new IllegalArgumentException("Пользователь не найден. " + id);
        }
        return users.get(id);
    }

    private Integer getNextId() {
        log.info("Вызван метод getNextId");
        int id = users
                .keySet()
                .stream()
                .mapToInt(obj -> obj)
                .max()
                .orElse(0) + 1;
        log.debug("Метод вернул " + id);
        return id;
    }
}
