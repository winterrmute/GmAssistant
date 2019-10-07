CREATE TABLE playlist (
    id  INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    content_id INTEGER NOT NULL,
    FOREIGN KEY (playlist_id)
      REFERENCES playlist (id)
)

CREATE TABLE content (
    id   INTEGER PRIMARY KEY,
    track_id INTEGER NOT NULL,
    FOREIGN KEY (track_id)
       REFERENCES track (id)
);

CREATE TABLE track (
    id  INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    artist TEXT NOT NULL,
    path TEXT NOT NULL,
    scene_id INTEGER,
    FOREIGN KEY (scene_id)
       REFERENCES scene (id)
)

CREATE TABLE scene (
    id INTEGER PRIMARY KEY,
    color TEXT NOT NULL,
    gamma INTEGER NOT NULL
    duration INTEGER NOT NULL
)