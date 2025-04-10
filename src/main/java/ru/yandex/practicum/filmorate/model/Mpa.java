package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Value;

@Value
public class Mpa {
    @Max(5)
    @Min(1)
    Integer id;
    String name;
}
