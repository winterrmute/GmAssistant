package com.wintermute.soundboard.services.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.soundboard.model.Track;
import com.wintermute.soundboard.services.database.DbManager;

import java.util.ArrayList;

/**
 * Represents database access object for track.
 *
 * @author wintermute
 */
public class TrackDao
{
    private static final String TABLE_NAME = "track";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String ARTIST_COLUMN = "artist";
    private static final String PATH_COLUMN = "path";
    private static final String SCENE_COLUMN = "scene_id";

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
    public String insert(Track track)
    {
        ContentValues values = new ContentValues();
        values.put(NAME_COLUMN, track.getName());
        values.put(ARTIST_COLUMN, track.getArtist());
        values.put(PATH_COLUMN, track.getPath());
        values.put(SCENE_COLUMN, track.getScene_id());

        dbWrite.insert(TABLE_NAME, null, values);
        return getIdByName(track.getName());
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
     * Select item by name.
     *
     * @param track to identify database entry by id.
     * @return selected track.
     */
    public Track getTrack(Track track)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(ID_COLUMN)
            .append("  =  '")
            .append(track.getId())
            .append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null)).get(0);
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
            track.setId(getColumnValue(cursor, ID_COLUMN));
            track.setName(getColumnValue(cursor, NAME_COLUMN));
            track.setArtist(getColumnValue(cursor, ARTIST_COLUMN));
            track.setPath(getColumnValue(cursor, PATH_COLUMN));
            track.setScene_id(getColumnValue(cursor, SCENE_COLUMN));
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
    public void update(Track track)
    {
        StringBuilder query = new StringBuilder("UPDATE ")
            .append(TABLE_NAME)
            .append(" SET ")
            .append(NAME_COLUMN)
            .append(" = '")
            .append(track.getName())
            .append("', ")
            .append(ARTIST_COLUMN)
            .append(" = '")
            .append(track.getArtist())
            .append("', ")
            .append(PATH_COLUMN)
            .append(" = '")
            .append(track.getPath())
            .append("', ")
            .append(SCENE_COLUMN)
            .append(" = '")
            .append(track.getScene_id())
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
    public void delete(Track track)
    {

        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(ID_COLUMN)
            .append(" = '")
            .append(track.getId())
            .append("'");
        dbWrite.execSQL(query.toString());
    }
}
