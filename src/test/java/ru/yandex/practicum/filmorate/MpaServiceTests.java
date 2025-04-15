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
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.genre.GenreService;
import ru.yandex.practicum.filmorate.service.mpa.MpaService;
import ru.yandex.practicum.filmorate.storage.genreStorage.dao.GenreDbStorage;

import java.util.List;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({GenreService.class, GenreDbStorage.class})
@Sql(scripts = {"/schema.sql", "/data.sql"})
public class MpaServiceTests {
    private final MpaService mpaService;
    private final List<String> allowedRatings = List.of("G", "PG", "PG-13", "R", "NC-17");

    @Test
    public void testGetMpaById() {
        Mpa mpa = mpaService.getRating(1);
        Assertions.assertNotNull(mpa);
        Assertions.assertNotNull(mpa.getId());
        Assertions.assertNotNull(mpa.getName());
        Assertions.assertEquals(1, (int) mpa.getId());
        Assertions.assertTrue(allowedRatings.contains(mpa.getName()));

        Assertions.assertThrows(NotFoundException.class, () -> mpaService.getRating(99999));
    }

    @Test
    public void testGetAllMpa() {
        List<Mpa> mpa = mpaService.getRatings();
        Assertions.assertNotNull(mpa);
        Assertions.assertFalse(mpa.isEmpty());
        Assertions.assertTrue(mpa.size() < 6);
        for (Mpa mpa1 : mpa) {
            Assertions.assertNotNull(mpa1);
            Assertions.assertNotNull(mpa1.getId());
            Assertions.assertNotNull(mpa1.getName());
            Assertions.assertTrue(allowedRatings.contains(mpa1.getName()));
        }
    }
}
