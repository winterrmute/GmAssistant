CREATE TABLE user_playlist (
    id  INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    playlist_id INTEGER NOT NULL,
    FOREIGN KEY (playlist_id)
      REFERENCES playlist (id)
)

CREATE TABLE playlist (
    id   INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    track_id INTEGER NOT NULL,
    FOREIGN KEY (track_id)
       REFERENCES track (id)
);

CREATE TABLE track (
    id  INTEGFER PRIMARY KEY,
    name TEXT NOT NULL,
    artist TEXT NOT NULL,
    path TEXT NOT NULL,
    duration INTEGER NOT NULL,
    light_id INTEGER,
    FOREIGN KEY (light_id)
       REFERENCES light (id)
)

CREATE TABLE light (
    id INTEGER PRIMARY KEY,
    color TEXT NOT NULL,
    gamma INTEGER NOT NULL
    duration INTEGER NOT NULL
)