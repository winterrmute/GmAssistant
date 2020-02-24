package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.helper.SceneDbModel;
import com.wintermute.gmassistant.helper.TrackDbModel;
import com.wintermute.gmassistant.model.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents database access object for track.
 *
 * @author wintermute
 */
public class TrackDao extends BaseDao
{
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
    public Long insert(ContentValues values)
    {
        return dbWrite.insert(TrackDbModel.TABLE_NAME.value(), null, values);
    }

    /**
     * Convinience method for getting track.
     *
     * @param key to identify database entry.
     * @param value column.
     * @return selected track.
     */
    public Map<String, Object> get(String key, String value)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TrackDbModel.TABLE_NAME.value())
            .append("  WHERE ")
            .append(key)
            .append("  =  '")
            .append(value)
            .append("'");
        ArrayList<Map<String, Object>> trackData = getTrackData(dbRead.rawQuery(query.toString(), null));
        return trackData.isEmpty() ? null : trackData.get(0);
    }

    /**
     * Get tracks referenced by given playlist id.
     *
     * @param id of playlist containing tracks.
     * @return List of Tracks
     */
    public ArrayList<Map<String, Object>> getReferencedTracks(String id)
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
    public ArrayList<Map<String, Object>> getAll()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM " + TrackDbModel.TABLE_NAME.value());
        return getTrackData(dbRead.rawQuery(query.toString(), null));
    }

    private ArrayList<Map<String, Object>> getTrackData(Cursor cursor)
    {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Map<String, Object> content = new HashMap<>();
            for (SceneDbModel element : SceneDbModel.values())
            {
                content.put(element.name().toLowerCase(), getKeyValue(cursor, element.value()));
            }
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
//        StringBuilder query = new StringBuilder("UPDATE ")
//            .append(TrackDbModel.TABLE_NAME.value())
//            .append(" SET ")
//            .append(updateQueryBuilder(createObject(track)))
//            .append(" WHERE id = '")
//            .append(track.getId())
//            .append("'");
//        dbWrite.execSQL(query.toString());
    }

    /**
     * Deletes row from database by id.
     *
     * @param id of track to remove.
     */
    public void deleteById(String id)
    {
        dbWrite.delete(TrackDbModel.TABLE_NAME.value(), TrackDbModel.ID.value() + " = " + id, new String[] {});
    }
}
