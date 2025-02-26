package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.yandex.practicum.filmorate.customAnnotatinos.ReleaseDate;
import ru.yandex.practicum.filmorate.interfaces.Update;

import java.time.LocalDate;


@Data
@Value
public class Film {
    @NotEmpty
    String name;
    @Size(max = 200)
    String description;
    @JsonFormat
    @NotNull
    @ReleaseDate
    LocalDate releaseDate;
    @NotNull
    @Min(0)
    Integer duration;
    @NonFinal
    @NotNull(groups = Update.class)
    Integer id;

    @JsonCreator
    public Film(@JsonProperty("name") String name,
                @JsonProperty("description") String description,
                @JsonProperty("releaseDate") LocalDate releaseDate,
                @JsonProperty("duration") Integer duration,
                @JsonProperty("id") Integer id) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.id = id;
    }
}