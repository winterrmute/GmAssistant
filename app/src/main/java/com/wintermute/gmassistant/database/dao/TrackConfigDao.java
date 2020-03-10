package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.TrackConfigDbModel;
import com.wintermute.gmassistant.view.model.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrackConfigDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public TrackConfigDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    public Long insert(ContentValues values)
    {
        return dbWrite.insert(TrackConfigDbModel.TABLE_NAME.value(), null, values);
    }

    /**
     * Get config for track in scene.
     *
     * @param sceneId containing track
     * @param trackId to get its config
     * @return selected track.
     */
    public Map<String, Long> get(Long sceneId, Long trackId)
    {
        StringBuilder query = new StringBuilder("SELECT volume, delay FROM ")
            .append(TrackConfigDbModel.TABLE_NAME.value())
            .append("  WHERE sceneId = '")
            .append(sceneId)
            .append("' AND trackId = '")
            .append(trackId)
            .append("'");
        ArrayList<Map<String, Long>> trackData = getTrackConfig(dbRead.rawQuery(query.toString(), null));
        return trackData.isEmpty() ? null : trackData.get(0);
    }

    private ArrayList<Map<String, Long>> getTrackConfig(Cursor query)
    {
        ArrayList<Map<String, Long>> result = new ArrayList<>();
        Map<String, Long> content = new HashMap<>();
        Long value;
        while (query.moveToNext())
        {
            for (String attr : TrackConfigDbModel.getValues())
            {
                if (!attr.equals(TrackConfigDbModel.TABLE_NAME.value()))
                {
                    value = query.getColumnIndex(attr) != -1L ? query.getLong(query.getColumnIndex(attr)) : -1L;
                    content.put(attr, value);
                }
            }
            result.add(content);
        }
        return result;
    }

    public void delete(Scene scene)
    {
        dbWrite.delete(TrackConfigDbModel.TABLE_NAME.value(),
            TrackConfigDbModel.SCENE_ID.value() + " = " + scene.getId().toString(), new String[] {});
    }
}
