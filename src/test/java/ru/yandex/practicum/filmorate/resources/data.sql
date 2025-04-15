MERGE INTO GENRE_NAMES (GENRE_NAME) KEY (GENRE_NAME) VALUES ('Комедия'),
                                                            ('Драма'),
                                                            ('Мультфильм'),
                                                            ('Триллер'),
                                                            ('Документальный'),
                                                            ('Боевик');
INSERT INTO films (FILM_ID, FILM_NAME, DESCRIPTION, RELEASE, DURATION, RATING_ID)
VALUES (DEFAULT, 'Фильм 1', 'Описание фильма 1', '1999-12-06', 189, 1);

INSERT INTO films (FILM_ID, FILM_NAME, DESCRIPTION, RELEASE, DURATION, RATING_ID)
VALUES (DEFAULT, 'Фильм 2', 'Описание фильма 2', '1994-09-23', 142, 3);

INSERT INTO films (FILM_ID, FILM_NAME, DESCRIPTION, RELEASE, DURATION, RATING_ID)
VALUES (DEFAULT, 'Фильм 3', 'Описание фильма 3', '1972-03-24', 175, 4);

INSERT INTO films (FILM_ID, FILM_NAME, DESCRIPTION, RELEASE, DURATION, RATING_ID)
VALUES (DEFAULT, 'Фильм 4', 'Описание фильма 4', '1994-06-23', 142, 2);

INSERT INTO films (FILM_ID, FILM_NAME, DESCRIPTION, RELEASE, DURATION, RATING_ID)
VALUES (DEFAULT, 'Фильм 5', 'Описание фильма 5', '1976-02-08', 114, 5);

INSERT INTO genre (genre_id, film_id)
VALUES (1, 1);
INSERT INTO genre (genre_id, film_id)
VALUES (3, 1);

INSERT INTO genre (genre_id, film_id)
VALUES (1, 2);

INSERT INTO genre (genre_id, film_id)
VALUES (1, 3);
INSERT INTO genre (genre_id, film_id)
VALUES (5, 3);

INSERT INTO genre (genre_id, film_id)
VALUES (1, 4);
INSERT INTO genre (genre_id, film_id)
VALUES (6, 4);

INSERT INTO genre (genre_id, film_id)
VALUES (7, 5);
INSERT INTO genre (genre_id, film_id)
VALUES (5, 5);