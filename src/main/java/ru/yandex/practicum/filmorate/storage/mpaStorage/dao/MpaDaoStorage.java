package ru.yandex.practicum.filmorate.storage.mpaStorage.dao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mappers.RatingMapper;
import ru.yandex.practicum.filmorate.storage.mpaStorage.MpaStorage;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class MpaDaoStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String getAllMpa = "SELECT * FROM rating";
        return jdbcTemplate.query(getAllMpa, new RatingMapper());
    }

    @Override
    public Mpa getRatingById(int id) {
        if (id < 1 || id > 5) {
            throw new NotFoundException("Mpa не найден");
        }
        String getRatingById = "SELECT * FROM rating WHERE rating_id = ?";
        return jdbcTemplate.queryForObject(getRatingById, new RatingMapper(), id);
    }
}
