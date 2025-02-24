package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class FilmsTest {
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
    public void addFilmTest() throws IOException, URISyntaxException, InterruptedException {
        Film film = new Film("film", "description", LocalDate.of(2023, 11, 28), 180, null);
        json = objectMapper.writeValueAsString(film);
        uri = new URI("http://localhost:8080/films");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        assertNotEquals(null, response.body());
    }

    @Test
    public void addFilmFailTest() throws URISyntaxException, IOException, InterruptedException {
        Film film = new Film("", "description", LocalDate.of(2023, 11, 28), 180, null);
        json = objectMapper.writeValueAsString(film);
        uri = new URI("http://localhost:8080/films");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        film = new Film("name", "descriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescriptiondescription", LocalDate.of(2023, 11, 28), 180, null);
        json = objectMapper.writeValueAsString(film);
        request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        film = new Film("film", "description", LocalDate.of(23, 11, 28), 180, null);
        json = objectMapper.writeValueAsString(film);
        request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        film = new Film("film", "description", LocalDate.of(2023, 11, 28), -180, null);
        json = objectMapper.writeValueAsString(film);
        request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

    }

    @Test
    public void updateFilmTest() throws IOException, URISyntaxException, InterruptedException {
        Film film = new Film("new film name", "new description", LocalDate.of(2023, 11, 28), 180, 1);
        json = objectMapper.writeValueAsString(film);
        uri = new URI("http://localhost:8080/films");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Film film1 = objectMapper.readValue(response.body(), Film.class);
        assertEquals(200, response.statusCode());
        assertEquals(film, film1);
    }

    @Test
    public void updateFilmFailTest() throws URISyntaxException, IOException, InterruptedException {
        json = "";
        uri = new URI("http://localhost:8080/films");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(json)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());

        Film film = new Film("new film name", "new description", LocalDate.of(2023, 11, 28), 180, null);
        json = objectMapper.writeValueAsString(film);
        request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").PUT(HttpRequest.BodyPublishers.ofString(json)).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(400, response.statusCode());
    }

    @Test
    public void getAllFilmsTest() throws URISyntaxException, IOException, InterruptedException {
        Film film = new Film("new film name", "new description", LocalDate.of(2023, 11, 28), 180, 1);
        uri = new URI("http://localhost:8080/films");

        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("Content-Type", "application/json").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Film[] filmsList = objectMapper.readValue(response.body(), Film[].class);

        assertEquals(200, response.statusCode());
        assertEquals(1, filmsList.length);
        assertEquals(film, filmsList[0]);
    }
}
