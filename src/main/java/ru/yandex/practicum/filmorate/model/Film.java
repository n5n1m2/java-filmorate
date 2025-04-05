package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.yandex.practicum.filmorate.customAnnotatinos.ReleaseDate;
import ru.yandex.practicum.filmorate.interfaces.Update;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


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
    @EqualsAndHashCode.Exclude
    Set<Like> likes = new HashSet<>();
    @EqualsAndHashCode.Exclude
    Set<FilmGenre> genres = new HashSet<>();
    @NotNull
    Rating rating;
    @NonFinal
    @NotNull(groups = Update.class)
    Integer id;

    @JsonCreator
    public Film(@JsonProperty("name") String name,
                @JsonProperty("description") String description,
                @JsonProperty("releaseDate") LocalDate releaseDate,
                @JsonProperty("duration") Integer duration,
                @JsonProperty("rating") Rating rating,
                @JsonProperty("id") Integer id) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rating = rating;
        this.id = id;
    }

    public void addLike(Like like) {
        likes.add(like);
    }

    public void removeLike(Like like) {
        likes.remove(like);
    }
}