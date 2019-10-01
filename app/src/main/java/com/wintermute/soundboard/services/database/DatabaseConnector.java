package com.wintermute.soundboard.services.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.UUID;

public class DatabaseConnector extends SQLiteOpenHelper
{
    private static final String SQL_CREATE_ENTRIES =
        "CREATE TABLE playlist (" + UUID.randomUUID().toString().replace("-", "")
            + "INTEGER PRIMARY KEY, song TEXT, path TEXT)";

    private static final String SQL_DELETE_ENTRIES =
        "DROP TABLE IF EXISTS playlist";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Playlist.db";

    public DatabaseConnector(Context ctx){
        super (ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }
}
