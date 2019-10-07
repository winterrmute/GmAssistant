package com.wintermute.soundboard.services.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.soundboard.model.Playlist;
import com.wintermute.soundboard.model.PlaylistContent;
import com.wintermute.soundboard.services.database.DbManager;

import java.util.ArrayList;

/**
 * Represents database access object for playlist content.
 *
 * @author wintermute
 */
public class PlaylistContentDao
{
    private static final String TABLE_NAME = "content";
    private static final String ID_COLUMN = "id";
    private static final String TRACK_COLUMN = "track_id";

    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public PlaylistContentDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    /**
     * Insert row into playlist table.
     */
    public void insert(Playlist playlist)
    {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, playlist.getId());
        values.put(TRACK_COLUMN, playlist.getName());
        if (playlist.getContentId() != -1)
        {
            values.put(TRACK_COLUMN, playlist.getContentId());
        }

        dbWrite.insert(TABLE_NAME, null, values);
    }

    public PlaylistContent getItemById(PlaylistContent playlistContent)
    {
        PlaylistContent result = new PlaylistContent();
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(ID_COLUMN)
            .append("  =  '")
            .append(playlistContent.getId())
            .append("'");

        Cursor cursor = dbRead.rawQuery(query.toString(), null);
        cursor.moveToFirst();
        result.setId(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(ID_COLUMN))));
        result.setTrack_id(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(TRACK_COLUMN))));
        return result;
    }

    public Cursor getAll()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_NAME);
        return dbRead.rawQuery(query.toString(), null);
    }

    /**
     * Insert row into playlist table.
     */
    public void update(PlaylistContent playlistContent)
    {
        StringBuilder query = new StringBuilder("UPDATE ")
            .append(TABLE_NAME)
            .append(" SET ")
            .append(ID_COLUMN)
            .append(" = '")
            .append(playlistContent.getId())
            .append("', ")
            .append(TRACK_COLUMN)
            .append(" = ")
            .append(playlistContent.getTrack_id())
            .append(" WHERE id = '")
            .append(playlistContent.getId())
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * Deletes a row identified by exactly matching provided id.
     */
    private void deleteById(String conditionColumn, String targetValue)
    {

        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(conditionColumn)
            .append(" = '")
            .append(targetValue)
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * Deletes all row in current table.
     */
    public void clearData()
    {

    }
}
