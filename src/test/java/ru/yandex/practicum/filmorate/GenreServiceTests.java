package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.storage.genreStorage.dao.GenreDbStorage;

import java.util.List;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreService.class, GenreDbStorage.class})
@Sql(scripts = {"/schema.sql", "/data.sql"})
public class GenreServiceTests {
    private final GenreService genreService;

    @Test
    public void testFindAll() {
        List<Genre> genres = genreService.getGenres();
        Assertions.assertEquals(6, genres.size());
        for (Genre genre : genres) {
            Assertions.assertNotNull(genre.getId());
            Assertions.assertNotNull(genre.getName());
        }
    }

    @Test
    public void testFindById() {
        Genre genre = genreService.getGenre(1);
        Assertions.assertNotNull(genre.getId());
        Assertions.assertNotNull(genre.getName());
        Assertions.assertThrows(NotFoundException.class, () -> genreService.getGenre(9999));
    }
}
