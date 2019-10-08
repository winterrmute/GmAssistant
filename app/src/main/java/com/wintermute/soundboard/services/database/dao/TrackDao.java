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
     */
    public void insert(Track track)
    {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, track.getId());
        values.put(NAME_COLUMN, track.getName());
        values.put(ARTIST_COLUMN, track.getArtist());
        values.put(PATH_COLUMN, track.getPath());
        values.put(SCENE_COLUMN, track.getScene_id());

        dbWrite.insert(TABLE_NAME, null, values);
    }

    /**
     * Select item by name.
     *
     * @param title to identify database entry.
     * @return selected track.
     */
    public Track getByName(String title)
    {
        Track result = new Track();
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(NAME_COLUMN)
            .append("  =  '")
            .append(title)
            .append("'");

        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Select item by name.
     *
     * @param id to identify database entry.
     * @return selected track.
     */
    public Track getById(long id)
    {
        Track result = new Track();
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(ID_COLUMN)
            .append("  =  '")
            .append(id)
            .append("'");

        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Get all records from table track as list.
     *
     * @return list of track names.
     */
    public ArrayList<Track> getAllTracks()
    {
        ArrayList<Track> playlist = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_NAME);
        Cursor cursor = dbRead.rawQuery(query.toString(), null);
        while (cursor.moveToNext())
        {
            playlist.add(mapObject(cursor));
        }
        return playlist;
    }

    private Track mapObject(Cursor cursor){
        Track result = new Track();
        while (cursor.moveToNext()){
                result.setId(cursor.getString(cursor.getColumnIndexOrThrow(ID_COLUMN)));
                result.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN)));
                result.setArtist(cursor.getString(cursor.getColumnIndexOrThrow(ARTIST_COLUMN)));
                result.setPath(cursor.getString(cursor.getColumnIndexOrThrow(PATH_COLUMN)));
                result.setScene_id(cursor.getString(cursor.getColumnIndexOrThrow(SCENE_COLUMN)));
        }
        return result;
    }

    /**
     * Insert row into track table.
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
            .append(" = ")
            .append(track.getArtist())
            .append(", ")
            .append(PATH_COLUMN)
            .append(" = '")
            .append(track.getPath())
            .append("', ")
            .append(SCENE_COLUMN).append(" = '").append(track.getScene_id())
            .append("' WHERE id = '")
            .append(track.getId())
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    public String getPath(String name){
        StringBuilder query = new StringBuilder("SELECT path FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(NAME_COLUMN)
            .append("  =  '")
            .append(name)
            .append("'");
        Cursor cursor = dbRead.rawQuery(query.toString(), null);

        return cursor.getString(cursor.getColumnIndexOrThrow(PATH_COLUMN));
    }

    /**
     * Convinience method to delete rows from database by different conditions.
     *
     * @param conditionColumn by which the delete operation will be specified.
     * @param targetValue to identify the row.
     * @param conditionArg specifies whether identifying should match or contain.
     */
    private void delete(String conditionColumn, String targetValue, String conditionArg)
    {
        targetValue = (conditionArg.equals("LIKE")) ? "%" + targetValue + "%" : targetValue;

        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(conditionColumn)
            .append(" ")
            .append(conditionArg)
            .append(" '")
            .append(targetValue)
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * Convinience method to determine whether deletion by name should exactly match or just contain string.
     *
     * @param conditionArg to define identifying method.
     */
    private void deleteByName(Track track, String conditionArg)
    {
        delete(NAME_COLUMN, track.getName(), conditionArg);
    }

    /**
     * Deletes a row identified by containing the provided string.
     */
    public void deleteByNameLike(Track track)
    {
        deleteByName(track, "LIKE");
    }

    /**
     * Deletes a row identified exactly matching the provided string.
     */
    public void deleteByNameMatching(Track track)
    {
        deleteByName(track, "=");
    }

    /**
     * Deletes a row identified by exactly matching provided id.
     */
    public void deleteById(Track track)
    {
        delete(ID_COLUMN, String.valueOf(track.getId()), "=");
    }

}
