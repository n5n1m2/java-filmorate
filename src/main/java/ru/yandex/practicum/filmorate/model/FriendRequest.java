package ru.yandex.practicum.filmorate.model;

import lombok.Value;

@Value
public class FriendRequest {
    int userId;
    int friendId;
    RequestStatus requestStatus;
}
