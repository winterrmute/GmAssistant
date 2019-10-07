package com.wintermute.soundboard.services.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.soundboard.model.Playlist;
import com.wintermute.soundboard.services.database.DbManager;

import java.util.ArrayList;

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
    private static final String CONTENT_COLUMN = "content_id";

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
    public void insert(Playlist playlist)
    {
        ContentValues values = new ContentValues();
        values.put(ID_COLUMN, playlist.getId());
        values.put(NAME_COLUMN, playlist.getName());
        if (playlist.getContentId() != -1)
        {
            values.put(CONTENT_COLUMN, playlist.getContentId());
        }

        dbWrite.insert(TABLE_NAME, null, values);
    }

    public Playlist getItemByName(String name)
    {
        Playlist result = new Playlist();
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(NAME_COLUMN)
            .append("  =  '")
            .append(name)
            .append("'");

        Cursor cursor = dbRead.rawQuery(query.toString(), null);
        cursor.moveToFirst();
        result.setId(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(ID_COLUMN))));
        result.setName(cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN)));
        result.setContentId(Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow(ID_COLUMN))));
        return result;
    }

    public Cursor getAll()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_NAME);
        return dbRead.rawQuery(query.toString(), null);
    }

    /**
     * Get all records from table playlist as list.
     *
     * @return list of names with names.
     */
    public ArrayList<String> getPlaylistNames()
    {
        ArrayList<String> playlist = new ArrayList<>();
        Cursor cursor = getAll();
        while (cursor.moveToNext())
        {
            playlist.add(cursor.getString(cursor.getColumnIndexOrThrow(NAME_COLUMN)));
        }
        return playlist;
    }

    /**
     * Insert row into playlist table.
     */
    public void update(Playlist playlist)
    {
        StringBuilder query = new StringBuilder("UPDATE ")
            .append(TABLE_NAME)
            .append(" SET ")
            .append(NAME_COLUMN)
            .append(" = '")
            .append(playlist.getName())
            .append("', ")
            .append(CONTENT_COLUMN)
            .append(" = ")
            .append(playlist.getContentId())
            .append(" WHERE id = '")
            .append(playlist.getId())
            .append("'");
        dbWrite.execSQL(query.toString());
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
    private void deleteByName(Playlist playlist, String conditionArg)
    {
        delete(NAME_COLUMN, playlist.getName(), conditionArg);
    }

    /**
     * Deletes a row identified by containing the provided string.
     */
    public void deleteByNameLike(Playlist playlist)
    {
        deleteByName(playlist, "LIKE");
    }

    /**
     * Deletes a row identified exactly matching the provided string.
     */
    public void deleteByNameMatching(Playlist playlist)
    {
        deleteByName(playlist, "=");
    }

    /**
     * Deletes a row identified by exactly matching provided id.
     */
    public void deleteById(Playlist playlist)
    {
        delete(ID_COLUMN, String.valueOf(playlist.getId()), "=");
    }

    /**
     * Deletes all row in current table.
     */
    public void clearData()
    {

    }
}
