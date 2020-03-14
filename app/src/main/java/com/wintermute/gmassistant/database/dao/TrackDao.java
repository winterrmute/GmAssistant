package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.Tags;
import com.wintermute.gmassistant.database.model.TrackDbModel;
import com.wintermute.gmassistant.view.model.Track;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents database access object for track.
 *
 * @author wintermute
 */
public class TrackDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public TrackDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
        dbWrite.execSQL("PRAGMA foreign_keys=ON;");
    }

    /**
     * Insert row into playlist table.
     *
     * @return id of inserted element.
     */
    public Long insert(ContentValues values)
    {
        return dbWrite.insertWithOnConflict(TrackDbModel.TABLE_NAME.value(), null, values,
            SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * Get track details.
     *
     * @param key database attribute
     * @param value of requested attribute.
     * @return selected track.
     */
    public Map<String, Object> getByAttribute(String key, String value)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TrackDbModel.TABLE_NAME.value())
            .append("  WHERE ")
            .append(key)
            .append("  =  '")
            .append(value)
            .append("'");
        List<Map<String, Object>> trackData = getTrackData(dbRead.rawQuery(query.toString(), null));
        return trackData.isEmpty() ? null : trackData.get(0);
    }

    /**
     * Get track details.
     *
     * @param id of requested track
     * @return selected track.
     */
    public Map<String, Object> get(Long id)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TrackDbModel.TABLE_NAME.value())
            .append("  WHERE id = '")
            .append(id)
            .append("'");
        List<Map<String, Object>> trackData = getTrackData(dbRead.rawQuery(query.toString(), null));
        return trackData.isEmpty() ? null : trackData.get(0);
    }

    /**
     * Get tracks referenced by given playlist id.
     *
     * @param id of playlist containing tracks.
     * @return List of Tracks
     */
    public List<Map<String, Object>> getReferencedTracks(String id)
    {
        StringBuilder query = new StringBuilder(
            "SELECT * FROM playlist pl, playlist_content ct, track tk WHERE tk.id = ct.track AND pl.id = ct.playlist "
                + "AND pl.id").append(" = '").append(id).append("'");
        return getTrackData(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Get all records from table track as list.
     *
     * @return list of track names.
     */
    public List<Map<String, Object>> getAll()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM " + TrackDbModel.TABLE_NAME.value());
        return getTrackData(dbRead.rawQuery(query.toString(), null));
    }

    private List<Map<String, Object>> getTrackData(Cursor query)
    {

        List<String> numericalValues =
            Arrays.asList(Tags.AMBIENCE.value(), Tags.MUSIC.value(), Tags.EFFECT.value(), TrackDbModel.DURATION.value(),
                TrackDbModel.ID.value());

        ArrayList<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> content;
        while (query.moveToNext())
        {
            content = new HashMap<>();
            for (String attr : TrackDbModel.getValues())
            {
                if (!attr.equals(TrackDbModel.TABLE_NAME.value()))
                {
                    if (numericalValues.contains(attr))
                    {
                        content.put(attr, getNumericalValue(query, attr));
                    } else
                    {
                        content.put(attr, getStringValue(query, attr));
                    }
                }
            }
            result.add(content);
        }
        return result;
    }

    private Long getNumericalValue(Cursor cursor, String attr)
    {

        if (cursor.getColumnIndex(attr) != -1)
        {
            return cursor.getLong(cursor.getColumnIndex(attr));
        }
        return -1L;
    }

    private String getStringValue(Cursor cursor, String attr)
    {
        if (cursor.getColumnIndex(attr) != -1)
        {
            return cursor.getString(cursor.getColumnIndex(attr));
        }
        return "-1";
    }
}
