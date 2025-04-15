package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserService.class})
@Sql(scripts = {"/schema.sql", "/data.sql"})
public class UserServiceTests {
    private final UserService userService;

    @Test
    public void testAddUser() {
        User user = new User(
                null,
                "mailtest@mail.ru",
                "login",
                "name",
                LocalDate.of(1988, 10, 1)
        );

        User created = userService.createUser(user);

        assertNotNull(created.getId());
        assertEquals("mailtest@mail.ru", created.getEmail());
        assertEquals("login", created.getLogin());
        assertEquals("name", created.getName());
        assertEquals(LocalDate.of(1988, 10, 1), created.getBirthday());
    }

    @Test
    public void getAllUsers() {
        userService.createUser(new User(null, "first@mail.ru", "login1", "name", LocalDate.of(1980, 1, 1)));
        userService.createUser(new User(null, "second@mail.ru", "login2", "name", LocalDate.of(1985, 2, 2)));

        List<User> users = userService.getAllUsers();

        assertEquals(5, users.size());
        assertEquals("login1", users.get(3).getLogin());
        assertEquals("login2", users.get(4).getLogin());
    }

    @Test
    public void testFailAddUserWithEmptyLogin() {
        User user = new User(
                null,
                "mail@mail.ru",
                " ",
                "name",
                LocalDate.of(1990, 1, 1)
        );

        assertThrows(ConstraintViolationException.class, () -> userService.createUser(user));
    }


    @Test
    public void testFailAddUserWithFutureBirthday() {
        User user = new User(
                null,
                "mail1@mail.ru",
                "login",
                "name",
                LocalDate.now().plusYears(1)
        );

        assertThrows(ConstraintViolationException.class, () -> userService.createUser(user));
    }


    @Test
    public void testUseLoginAsNameIfNameIsNullOrBlank() {
        User user = new User(
                null,
                "mail2@mail.ru",
                "login",
                null,
                LocalDate.of(1995, 5, 5)
        );

        User created = userService.createUser(user);

        assertEquals("login", created.getName());
    }

    @Test
    public void testUpdateUser() {
        User original = new User(null, "test@mail.ru", "login", null, LocalDate.of(1980, 8, 20));
        User created = userService.createUser(original);

        User updated = new User(
                created.getId(),
                "new@mail.ru",
                "updatedLogin",
                "name",
                LocalDate.of(1990, 1, 1)
        );

        User result = userService.updateUser(updated);

        assertEquals(created.getId(), result.getId());
        assertEquals("new@mail.ru", result.getEmail());
        assertEquals("updatedLogin", result.getLogin());
        assertEquals("name", result.getName());
        assertEquals(LocalDate.of(1990, 1, 1), result.getBirthday());
    }

    @Test
    public void testFailUpdateUnknownUser() {
        User unknown = new User(
                9999,
                "ghost@mail.com",
                "ghostLogin",
                "name",
                LocalDate.of(2000, 1, 1)
        );

        assertThrows(NotFoundException.class, () -> userService.updateUser(unknown));
    }
}
