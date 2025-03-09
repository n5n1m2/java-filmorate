package ru.yandex.practicum.filmorate;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class UsersTest {
    private final HttpClient client = HttpClient.newBuilder().build();
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private String json;
    private URI uri;

    @BeforeAll
    public static void startServer() {
        FilmorateApplication.main(new String[0]);
    }

    @AfterAll
    public static void stopServer() {
        FilmorateApplication.stopServer();
    }

    @Test
    public void userCreateTest() throws URISyntaxException, IOException, InterruptedException {
        User user = new User(null, "email@mail.ru", "login", "name", LocalDate.of(1980, 11, 10));
        json = objectMapper.writeValueAsString(user);
        uri = new URI("http://localhost:8080/users");
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        User user1 = objectMapper.readValue(response.body(), User.class);
        assertEquals(200, response.statusCode());
        assertNotEquals(null, response.body());
        assertEquals(user.getName(), user1.getName());
        assertEquals(user.getLogin(), user1.getLogin());
        assertEquals(user.getEmail(), user1.getEmail());
        assertEquals(user.getBirthday(), user1.getBirthday());
        assertNotEquals(user.getId(), user1.getId());
    }

    @Test
    public void userCreateFailTest() throws IOException, InterruptedException, URISyntaxException {
        User user = new User(null, "email@mail.ru", "login", "name", LocalDate.of(2480, 11, 10));
        json = objectMapper.writeValueAsString(user);
        uri = new URI("http://localhost:8080/users");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        user = new User(null, "ru.@mail", "login", "name", LocalDate.of(1980, 11, 10));
        json = objectMapper.writeValueAsString(user);

        request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        user = new User(null, "email@mail.ru", "login name", "name", LocalDate.of(1980, 11, 10));
        json = objectMapper.writeValueAsString(user);

        request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void createUserWithEmptyNameTest() throws IOException, URISyntaxException, InterruptedException {
        User user = new User(null, "email@mail.ru", "login", null, LocalDate.of(1980, 11, 10));
        json = objectMapper.writeValueAsString(user);
        uri = new URI("http://localhost:8080/users");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
    }

    @Test
    public void updateUserTest() throws IOException, InterruptedException, URISyntaxException {
        User user = new User(1, "email@mail.ru", "newLogin", "newName", LocalDate.of(1980, 11, 10));
        json = objectMapper.writeValueAsString(user);
        uri = new URI("http://localhost:8080/users");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        User user1 = objectMapper.readValue(response.body(), User.class);
        assertEquals(200, response.statusCode());
        assertEquals(user, user1);
    }

    @Test
    public void updateUserFailTest() throws IOException, InterruptedException, URISyntaxException {
        User user = new User(97866, "email@mail.ru", "newLogin", "newName", LocalDate.of(1980, 11, 10));
        json = objectMapper.writeValueAsString(user);
        uri = new URI("http://localhost:8080/users");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void userGetAllTest() throws URISyntaxException, IOException, InterruptedException {
        User user = new User(1, "email@mail.ru", "newLogin", "newName", LocalDate.of(1980, 11, 10));
        uri = new URI("http://localhost:8080/users");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        User[] userList = objectMapper.readValue(response.body(), User[].class);
        assertEquals(200, response.statusCode());
        assertEquals(1, userList.length);
        assertEquals(user, userList[0]);
    }
}