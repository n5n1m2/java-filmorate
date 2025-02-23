package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;
import lombok.experimental.NonFinal;
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
    LocalDate releaseDate;
    @NotNull
    @Min(0)
    Integer duration;
    @NonFinal
    @NotNull(groups = Update.class)
    Integer id;

    @AssertTrue
    public boolean isReleaseDateValid() {
        return releaseDate != null && releaseDate.isAfter(LocalDate.of(1895, 12, 28));
    }
}
