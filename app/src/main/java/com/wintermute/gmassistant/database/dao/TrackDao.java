package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.dto.Track;

import java.util.ArrayList;

/**
 * Represents database access object for track.
 *
 * @author wintermute
 */
public class TrackDao
{
    private static final String TABLE_NAME = "track";
    private static final String ID_KEY = "id";
    private static final String NAME_KEY = "name";
    private static final String ARTIST_KEY = "artist";
    private static final String TAG_KEY = "tag";
    private static final String PATH_KEY = "path";

    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public TrackDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    /**
     * Insert row into playlist table. TODO: refactor me
     *
     * @return id of inserted element.
     */
    public String insert(Track track)
    {
        if (computeIdIfAbsent(track.getName()) != null)
        {
            return computeIdIfAbsent(track.getName());
        } else
        {
            ContentValues values = new ContentValues();
            values.put(NAME_KEY, track.getName());
            values.put(ARTIST_KEY, track.getArtist());
            values.put(TAG_KEY, track.getTag());
            values.put(PATH_KEY, track.getPath());

            dbWrite.insert(TABLE_NAME, null, values);
            return computeIdIfAbsent(track.getName());
        }
    }

    /**
     * Select item by name.
     *
     * @param title to identify database entry.
     * @return selected track.
     */
    private String computeIdIfAbsent(String title)
    {
        StringBuilder query = new StringBuilder("SELECT id FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(NAME_KEY)
            .append("  =  '")
            .append(title)
            .append("'");
        try
        {
            return mapObject(dbRead.rawQuery(query.toString(), null)).get(0).getId();
        } catch (IndexOutOfBoundsException e)
        {
            return null;
        }
    }

    /**
     * Convinience method for getting track.
     *
     * @param key to identify database entry.
     * @param value column.
     * @return selected track.
     */
    Track getTrack(String key, String value)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(key)
            .append("  =  '")
            .append(value)
            .append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null)).get(0);
    }

    public Track getById(String id)
    {
        return getTrack(ID_KEY, id);
    }

    /**
     * Get track by path.
     *
     * @param path of track to find.
     * @return selected track.
     */
    public Track getTrackByPath(String path)
    {
        return getTrack(PATH_KEY, path);
    }

    /**
     * Get track by id if present or new object.
     *
     * @param id of track to find.
     * @return queried track if present, empty track if id was null.
     */
    public Track computeTrackIfAbsent(String id)
    {
        if (id != null)
        {
            return getTrack(ID_KEY, id);
        } else
        {
            Track result = new Track();
            result.setName("NONE");
            return result;
        }
    }

    /**
     * Get tracks referenced by given playlist id.
     *
     * @param id of playlist containing tracks.
     * @return List of Tracks
     */
    public ArrayList<Track> getReferencedTracks(String id)
    {
        StringBuilder query = new StringBuilder(
            "SELECT * FROM playlist pl, playlist_content ct, track tk WHERE tk.id = ct.track AND pl.id = ct.playlist "
                + "AND pl.id").append(" = '").append(id).append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Get all records from table track as list.
     *
     * @return list of track names.
     */
    public ArrayList<Track> getAll()
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
    private ArrayList<Track> mapObject(Cursor cursor)
    {
        ArrayList<Track> result = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Track track = new Track();
            track.setId(getKeyValue(cursor, ID_KEY));
            track.setName(getKeyValue(cursor, NAME_KEY));
            track.setArtist(getKeyValue(cursor, ARTIST_KEY));
            track.setTag(getKeyValue(cursor, TAG_KEY));
            track.setPath(getKeyValue(cursor, PATH_KEY));
            result.add(track);
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
    private String getKeyValue(Cursor cursor, String column)
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
    public void update(Track track)
    {
        StringBuilder query = new StringBuilder("UPDATE ")
            .append(TABLE_NAME)
            .append(" SET ")
            .append(NAME_KEY)
            .append(" = '")
            .append(track.getName())
            .append("', ")
            .append(ARTIST_KEY)
            .append(" = '")
            .append(track.getArtist())
            .append("', ")
            .append(TAG_KEY)
            .append(" = '")
            .append(track.getTag())
            .append("', ")
            .append(PATH_KEY)
            .append(" = '")
            .append(track.getPath())
            .append("' WHERE id = '")
            .append(track.getId())
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * Deletes row from database by id.
     *
     * @param id of track to remove.
     */
    public void deleteById(String id)
    {
        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(ID_KEY)
            .append(" = '")
            .append(id)
            .append("'");
        dbWrite.execSQL(query.toString());
    }
}
