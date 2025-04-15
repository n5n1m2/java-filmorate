MERGE INTO GENRE_NAMES (GENRE_NAME) KEY (GENRE_NAME) VALUES ('Комедия'),
                                                            ('Драма'),
                                                            ('Мультфильм'),
                                                            ('Триллер'),
                                                            ('Документальный'),
                                                            ('Боевик');
MERGE INTO RATING (RATING_NAME) KEY (RATING_NAME) VALUES ('G'),
                                                         ('PG'),
                                                         ('PG-13'),
                                                         ('R'),
                                                         ('NC-17');