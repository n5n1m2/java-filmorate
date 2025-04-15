package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.yandex.practicum.filmorate.interfaces.Update;

import java.time.LocalDate;


@Data
@Value
public class User {
    @NotEmpty
    @Email
    String email;
    @Pattern(regexp = "^\\S+$")
    @NotEmpty
    String login;
    @JsonFormat
    @Past
    @NotNull
    LocalDate birthday;
    @JsonIgnore
    @NonFinal
    @NotNull(groups = Update.class)
    Integer id;
    @NonFinal
    String name;

    @JsonCreator
    public User(
            @JsonProperty("id") Integer id,
            @JsonProperty("email") String email,
            @JsonProperty("login") String login,
            @JsonProperty("name") String name,
            @JsonProperty("birthday") LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name != null) {
            this.name = name;
        } else {
            this.name = login;
        }
        this.birthday = birthday;
    }
}