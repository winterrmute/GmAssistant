package com.wintermute.gmassistant.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.stream.Stream;

public class DbManager extends SQLiteOpenHelper
{

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "gmassistant";

    private static final String PLAYLISTS =
        "CREATE TABLE playlists ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL)";

    private static final String TRACKS =
        "CREATE TABLE tracks ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, path TEXT UNIQUE, duration INT, artist"
            + " TEXT)";

    private static final String PLAYLISTS_CONTENT =
        "CREATE TABLE playlists_content ( playlist INTEGER, track INTEGER, scene INTEGER, FOREIGN KEY (playlist) "
            + "REFERENCES playlists (id), FOREIGN KEY (track) REFERENCES tracks (id), FOREIGN KEY "
            + "(scene) REFERENCES scenes (id) )";

    private static final String SCENES =
        "CREATE TABLE scenes ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, light INTEGER, effect INTEGER, "
            + "music INTEGER, ambience INTEGER)";

    private static final String SCENE_TRACK_CONFIGS =
        "CREATE TABLE scene_track_configs ( sceneId INTEGER, trackId INTEGER, volume INTEGER, delay"
            + " INTEGER, FOREIGN KEY (sceneId) REFERENCES scenes (id) ON DELETE CASCADE, FOREIGN KEY (trackId) REFERENCES tracks (id))";

    private static final String LIGHTS =
        "CREATE TABLE lights ( id INTEGER PRIMARY KEY AUTOINCREMENT, color REAL, brightness REAL)";

    private static final String HUE_BRIDGES =
        "CREATE TABLE hue_bridges ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, ip TEXT UNIQUE, username TEXT)";

    private static final String HUE_BULBS =
        "CREATE TABLE hue_bulbs ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, type TEXT)";

    private static final String LIBRARY =
        "CREATE TABLE library ( id INTEGER PRIMARY KEY AUTOINCREMENT, path TEXT, tag TEXT, recursively TEXT)";

    private static final String EFFECTS =
        "CREATE TABLE effects ( trackId INTEGER, groupId INTEGER, FOREIGN KEY (trackId) REFERENCES tracks (id) ON "
            + "DELETE CASCADE, FOREIGN KEY" + " (groupId) REFERENCES groups (id) ON DELETE CASCADE )";

    private static final String GROUPS = "CREATE TABLE groups (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS user_playlist";

    public DbManager(Context ctx)
    {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        Stream
            .of(PLAYLISTS, TRACKS, PLAYLISTS_CONTENT, SCENES, SCENE_TRACK_CONFIGS, LIGHTS, HUE_BRIDGES, HUE_BULBS, LIBRARY, EFFECTS, GROUPS)
            .forEach(db::execSQL);
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
