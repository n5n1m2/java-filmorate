package ru.yandex.practicum.filmorate.model;

import lombok.Value;

@Value
public class FriendRequest {
    int user_id;
    int friend_id;
    RequestStatus request_status;
}
