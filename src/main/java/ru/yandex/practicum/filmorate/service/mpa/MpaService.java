package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpaStorage.MpaStorage;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public List<Mpa> getRatings() {
        return mpaStorage.getAllMpa();
    }

    public Mpa getRating(final int id) {
        return mpaStorage.getRatingById(id);
    }
}
