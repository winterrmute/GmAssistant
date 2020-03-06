package com.wintermute.gmassistant.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.stream.Stream;

public class DbManager extends SQLiteOpenHelper
{

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "soundboard";

    private static final String PLAYLIST =
        "CREATE TABLE playlist ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)";

    private static final String TRACK =
        "CREATE TABLE track ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, path TEXT UNIQUE, duration INT, artist"
            + " TEXT)";

    private static final String PLAYLIST_CONTENT =
        "CREATE TABLE playlist_content ( playlist INTEGER, track INTEGER, scene INTEGER, FOREIGN KEY (playlist) "
            + "REFERENCES playlist (id), FOREIGN KEY (track) REFERENCES track (id), FOREIGN KEY "
            + "(scene) REFERENCES scene (id) )";

    private static final String SCENE =
        "CREATE TABLE scene ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, light INTEGER, effect INTEGER, "
            + "music INTEGER, ambience INTEGER, FOREIGN KEY (light) REFERENCES light (id), FOREIGN KEY (effect) "
            + "REFERENCES scene_track_config (id), FOREIGN KEY (music) REFERENCES scene_track_config (id), FOREIGN "
            + "KEY (ambience) REFERENCES scene_track_config (id))";

    private static final String SCENE_TRACK_CONFIG =
        "CREATE TABLE scene_track_config ( id INTEGER PRIMARY KEY AUTOINCREMENT, trackId INTEGER, volume INTEGER, delay"
            + " INTEGER, FOREIGN KEY (trackId) REFERENCES track (id))";

    private static final String LIGHT =
        "CREATE TABLE light ( id INTEGER PRIMARY KEY AUTOINCREMENT, color REAL, brightness REAL)";

    private static final String LIBRARY =
        "CREATE TABLE library ( id INTEGER PRIMARY KEY AUTOINCREMENT, path TEXT, tag TEXT, recursively TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS user_playlist";

    public DbManager(Context ctx)
    {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Stream.of(PLAYLIST, TRACK, PLAYLIST_CONTENT, SCENE, SCENE_TRACK_CONFIG, LIGHT, LIBRARY).forEach(db::execSQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        onUpgrade(db, oldVersion, newVersion);
    }
}
