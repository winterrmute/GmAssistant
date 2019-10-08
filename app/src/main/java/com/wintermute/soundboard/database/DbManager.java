package com.wintermute.soundboard.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper
{

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "soundboard";

    //TODO: find better way that is less failure tolerant
    private static final String CREATE_PLAYLIST =
        "CREATE TABLE IF NOT EXISTS playlist ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, content_id "
            + "INTEGER, FOREIGN KEY (content_id)  "
            + "REFERENCES playlist (id))";

    private static final String CREATE_TRACK =
        "CREATE TABLE IF NOT EXISTS track ( id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, artist TEXT, path TEXT, "
            + "scene_id, FOREIGN KEY (scene_id) REFERENCES scene (id) )";
    private static final String CREATE_PLAYLIST_CONTENT =
        "CREATE TABLE IF NOT EXISTS playlist_content ( id INTEGER, track_id INTEGER, FOREIGN KEY "
            + "(track_id) REFERENCES track (id) )";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS user_playlist";

    public DbManager(Context ctx)
    {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_PLAYLIST);
        db.execSQL(CREATE_TRACK);
        db.execSQL(CREATE_PLAYLIST_CONTENT);
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
