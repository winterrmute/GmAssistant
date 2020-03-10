package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.view.model.Playlist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents database access object for playlist.
 *
 * @author wintermute
 */
public class PlaylistDao extends BaseDao
{
    private static final String TABLE_NAME = "playlist";
    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";

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
    public long insert(Playlist playlist)
    {
        Map<String, Object> object = createObject(playlist);
        ContentValues values = getContentValues(object);
        return dbWrite.insert(TABLE_NAME, null, values);
    }

    /**
     * @return map containing non null values.
     */
    private Map<String, Object> createObject(Playlist content)
    {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put(ID_KEY, content.getId());
        obj.put(NAME_KEY, content.getName());
        return removeEmptyValues(obj);
    }

    /**
     * Select item by id.
     *
     * @param id to get from database.
     * @return specified playlist.
     */
    public Playlist getPlaylist(String id)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(ID_KEY)
            .append("  =  '")
            .append(id)
            .append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null)).get(0);
    }

    /**
     * Translates the data from database to java objects.
     *
     * @param cursor to iterate over database rows.
     * @return list of track objects.
     */
    private ArrayList<Playlist> mapObject(Cursor cursor)
    {
        ArrayList<Playlist> result = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Playlist playlist = new Playlist();
            playlist.setId(getColumnValue(cursor, ID_KEY));
            playlist.setName(getColumnValue(cursor, NAME_KEY));
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
     * @return all existing playlists.
     */
    public ArrayList<Playlist> getAll()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_NAME);
        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * @return list of playlist names.
     */
    public List<String> getPlaylistNames()
    {
        return getAll().stream().map(Playlist::getName).collect(Collectors.toList());
    }

    /**
     * Insert row into playlist table.
     */
    public void update(Playlist playlist)
    {
        StringBuilder query = new StringBuilder("UPDATE ")
            .append(TABLE_NAME)
            .append(" SET ")
            .append(updateQueryBuilder(createObject(playlist)))
            .append(" WHERE id = '")
            .append(playlist.getId())
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * Deletes row by id.
     *
     * @param id of playlist to delete.
     */
    public void deleteById(String id)
    {
        dbWrite.execSQL(getDeleteQuery(TABLE_NAME, ID_KEY, id));
    }
}
