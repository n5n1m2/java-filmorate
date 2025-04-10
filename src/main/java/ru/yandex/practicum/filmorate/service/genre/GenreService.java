package ru.yandex.practicum.filmorate.service.genre;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genreStorage.GenreStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> getGenres() {
        return genreStorage.getAllGenre();
    }

    public Genre getGenre(final int id) {
        return genreStorage.getGenre(id);
    }
}
