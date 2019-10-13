package com.wintermute.soundboard.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.soundboard.database.dto.PlaylistDto;
import com.wintermute.soundboard.database.DbManager;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents database access object for playlist.
 *
 * @author wintermute
 */
public class PlaylistDao
{
    private static final String TABLE_NAME = "playlist";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";

    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public PlaylistDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    /**
     * Insert row into playlist table.
     */
    public String insert(PlaylistDto playlist)
    {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, playlist.getId());
        values.put(NAME_COLUMN, playlist.getName());

        dbWrite.insert(TABLE_NAME, null, values);
        return getIdByName(playlist.getName());
    }

    /**
     * Select item by id.
     *
     * @param id to get from database.
     * @return specified playlist.
     */
    public PlaylistDto getPlaylist(String id)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(ID_COLUMN)
            .append("  =  '")
            .append(id)
            .append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null)).get(0);
    }

    /**
     * Select item by name.
     *
     * @param title to identify database entry.
     * @return selected track.
     */
    private String getIdByName(String title)
    {
        StringBuilder query = new StringBuilder("SELECT id FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(NAME_COLUMN)
            .append("  =  '")
            .append(title)
            .append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null)).get(0).getId();
    }

    /**
     * Translates the data from database to java objects.
     *
     * @param cursor to iterate over database rows.
     * @return list of track objects.
     */
    private ArrayList<PlaylistDto> mapObject(Cursor cursor)
    {
        ArrayList<PlaylistDto> result = new ArrayList<>();
        while (cursor.moveToNext())
        {
            PlaylistDto playlist = new PlaylistDto();
            playlist.setId(getColumnValue(cursor, ID_COLUMN));
            playlist.setName(getColumnValue(cursor, NAME_COLUMN));
            result.add(playlist);
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
     * Select all from playlist.
     * TODO: Return list instead of cursor
     *
     * @return query result as Cursor.
     */
    public ArrayList<PlaylistDto> getAll()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_NAME);
        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Get all records from table playlist as list.
     *
     * @return list of playlist names.
     */
    public List<String> getPlaylistNames()
    {
        return getAll().stream().map(PlaylistDto::getName).collect(Collectors.toList());
    }

    /**
     * Insert row into playlist table.
     */
    public void update(PlaylistDto playlist)
    {
        StringBuilder query = new StringBuilder("UPDATE ")
            .append(TABLE_NAME)
            .append(" SET ")
            .append(NAME_COLUMN)
            .append(" = '")
            .append(playlist.getName())
            .append("' WHERE id = '")
            .append(playlist.getId())
            .append("'");
        String qry = query.toString();
        dbWrite.execSQL(query.toString());
    }

    /**
     * Deletes row by id.
     *
     * @param playlist to deleteByTrackId by id.
     */
    public void delete(PlaylistDto playlist)
    {
        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(ID_COLUMN)
            .append(" = '")
            .append(playlist.getId())
            .append("'");
        dbWrite.execSQL(query.toString());
    }
}
