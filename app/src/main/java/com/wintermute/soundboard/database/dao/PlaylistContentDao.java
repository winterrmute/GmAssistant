package com.wintermute.soundboard.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.soundboard.database.DbManager;
import com.wintermute.soundboard.database.dto.PlaylistContentDto;

import java.util.ArrayList;
import java.util.List;

public class PlaylistContentDao
{
    private static final String TABLE_NAME = "playlist_content";
    private static final String PLAYLIST_COLUMN = "playlist";
    private static final String TRACK_COLUMN = "track";

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
     *
     * @return id of inserted element.
     */
    public void insert(PlaylistContentDto content)
    {
        ContentValues values = new ContentValues();
        values.put(PLAYLIST_COLUMN, content.getPlaylist());
        values.put(TRACK_COLUMN, content.getTrack());
        dbWrite.insert(TABLE_NAME, null, values);
    }

    /**
     * Select item by id.
     *
     * @param playlistId to identify database entry by id.
     * @return selected track.
     */
    public List<PlaylistContentDto> getPlaylistContent(String playlistId)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(PLAYLIST_COLUMN)
            .append("  =  '")
            .append(playlistId)
            .append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Get all records from table track as list.
     *
     * @return list of track names.
     */
    public ArrayList<PlaylistContentDto> getAll()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_NAME);
        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Translates the data from database to java objects.
     *
     * @param cursor to iterate over database rows.
     * @return list of track objects.
     */
    private ArrayList<PlaylistContentDto> mapObject(Cursor cursor)
    {
        ArrayList<PlaylistContentDto> result = new ArrayList<>();
        while (cursor.moveToNext())
        {
            PlaylistContentDto content = new PlaylistContentDto();
            content.setPlaylist(getColumnValue(cursor, PLAYLIST_COLUMN));
            content.setTrack(getColumnValue(cursor, TRACK_COLUMN));
            result.add(content);
        }
        return result;
    }

    /**
     * Safely gets data from database.
     *
     * @param cursor to pick data from database
     * @param column containing value
     * @return value stored in db if possible, otherwise "-1"
     */
    private String getColumnValue(Cursor cursor, String column)
    {
        if (cursor.getColumnIndex(column) != -1)
        {
            return cursor.getString(cursor.getColumnIndex(column));
        }
        return "-1";
    }

    /**
     * Uptdate row in track table.
     */
    public void update(PlaylistContentDto content)
    {
        StringBuilder query = new StringBuilder("UPDATE ")
            .append(TABLE_NAME)
            .append(" SET ")
            .append(PLAYLIST_COLUMN)
            .append(" = '")
            .append(content.getPlaylist())
            .append("', ")
            .append(TRACK_COLUMN)
            .append("' = ")
            .append(content.getTrack())
            .append("' WHERE playlist = '")
            .append(content.getPlaylist())
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * Deletes row from database by id.
     *
     * @param trackId to remove from database.
     */
    public void deleteByTrackId(String trackId)
    {

        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(TRACK_COLUMN)
            .append(" = '")
            .append(trackId)
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * Deletes row from database by id.
     *
     * @param trackId to remove from database.
     */
    public void deleteByPlaylistId(String trackId)
    {

        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(PLAYLIST_COLUMN)
            .append(" = '")
            .append(trackId)
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * Deletes row from database by id.
     *
     * @param trackId to remove from database.
     */
    public void deleteTrackFromPlaylist(String playlistId, String trackId)
    {

        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(PLAYLIST_COLUMN)
            .append(" = '")
            .append(playlistId)
            .append(" ' AND ")
            .append(TRACK_COLUMN)
            .append(" = '")
            .append(trackId)
            .append("'");
        dbWrite.execSQL(query.toString());
    }
}
