package com.wintermute.soundboard.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.soundboard.database.DbManager;
import com.wintermute.soundboard.database.dto.TrackDto;

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
    private static final String SCENE_KEY = "scene_id";

    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public TrackDao(Context ctx)
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
    public String insert(TrackDto track)
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
            values.put(SCENE_KEY, track.getSceneId());

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

    public String getTag(String id)
    {
        StringBuilder query = new StringBuilder("SELECT tag FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(ID_KEY)
            .append(" = '")
            .append(id)
            .append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null)).get(0).getTag();
    }

    /**
     * Convinience method for getting track.
     *
     * @param key to identify database entry.
     * @param value column.
     * @return selected track.
     */
    private TrackDto getTrack(String key, String value)
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

    /**
     * Get track by path.
     *
     * @param path of track to find.
     * @return selected track.
     */
    public TrackDto getTrackByPath(String path)
    {
        return getTrack(PATH_KEY, path);
    }

    /**
     * Get track by id.
     *
     * @param id of track to find.
     * @return selected track.
     */
    public TrackDto getTrackById(String id)
    {
        return getTrack(ID_KEY, id);
    }

    /**
     * Get tracks referenced by given playlist id.
     *
     * @param id of playlist containing tracks.
     * @return List of Tracks
     */
    public ArrayList<TrackDto> getReferencedTracks(String id)
    {
        StringBuilder query = new StringBuilder(
            "SELECT * FROM playlist pl, playlist_content ct, track tk WHERE tk.id = ct.track AND pl.id = ct.playlist "
                + "AND pl.id").append(" = '").append(id).append("'");
//        dbRead.rawQuery(query.toString(), null);
        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Get all records from table track as list.
     *
     * @return list of track names.
     */
    public ArrayList<TrackDto> getAll()
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
    private ArrayList<TrackDto> mapObject(Cursor cursor)
    {
        ArrayList<TrackDto> result = new ArrayList<>();
        while (cursor.moveToNext())
        {
            TrackDto track = new TrackDto();
            track.setId(getKeyValue(cursor, ID_KEY));
            track.setName(getKeyValue(cursor, NAME_KEY));
            track.setArtist(getKeyValue(cursor, ARTIST_KEY));
            track.setTag(getKeyValue(cursor, TAG_KEY));
            track.setPath(getKeyValue(cursor, PATH_KEY));
            track.setSceneId(getKeyValue(cursor, SCENE_KEY));
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
    public void update(TrackDto track)
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
            .append("', ")
            .append(SCENE_KEY)
            .append(" = '")
            .append(track.getSceneId())
            .append("' WHERE id = '")
            .append(track.getId())
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * Deletes row from database by id.
     *
     * @param track to remove from database.
     */
    void delete(TrackDto track)
    {
        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(ID_KEY)
            .append(" = '")
            .append(track.getId())
            .append("'");
        dbWrite.execSQL(query.toString());
    }
}
