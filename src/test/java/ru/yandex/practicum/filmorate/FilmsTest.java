package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;

public class FilmsTest {
    private HttpClient client = HttpClient.newBuilder().build();
    private ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
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
    public void addFilmTest(){
    }
}
