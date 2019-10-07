package com.wintermute.soundboard.services.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbManager extends SQLiteOpenHelper
{

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "soundboard";

    //TODO: find better way that is less failure tolerant
    private static final String CREATE_PLAYLIST =
        "CREATE TABLE playlist ( id INTEGER PRIMARY KEY, name TEXT NOT NULL, content_id INTEGER, FOREIGN KEY (content_id)  "
            + "REFERENCES playlist (id))";

    private static final String CREATE_TRACK = "CREATE TABLE track ( id INTEGER PRIMARY KEY, name TEXT, artist TEXT, path TEXT, scene_id, FOREIGN KEY (scene_id) REFERENCES scene (id) )";

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

    public void dropTable(SQLiteDatabase db, String tableName)
    {
        db.execSQL("DROP TABLE " + tableName);
    }

    public void getItemById(SQLiteDatabase db, String tableName,long itemId)
    {
        db.execSQL("SELECT id FROM " + tableName + "WHERE id = " + itemId);
    }
}
