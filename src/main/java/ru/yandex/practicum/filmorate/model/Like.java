package ru.yandex.practicum.filmorate.model;


import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class Like {
    Integer filmId;
    Integer userId;
    @NotNull
    Integer likeId;
}
