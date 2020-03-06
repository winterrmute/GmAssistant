package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.helper.SceneTrackDbModel;
import com.wintermute.gmassistant.helper.TrackDbModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SceneTrackDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public SceneTrackDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    public Long insert(ContentValues values)
    {
        return dbWrite.insertWithOnConflict(SceneTrackDbModel.TABLE_NAME.value(), null, values,
            SQLiteDatabase.CONFLICT_IGNORE);
    }

    /**
     * Get config for track in scene.
     *
     * @param key to identify database cell.
     * @param value column.
     * @return selected track.
     */
    public Map<String, Long> get(String key, String value)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TrackDbModel.TABLE_NAME.value())
            .append("  WHERE ")
            .append(key)
            .append("  =  '")
            .append(value)
            .append("'");
        ArrayList<Map<String, Long>> trackData = getTrackConfig(dbRead.rawQuery(query.toString(), null));
        return trackData.isEmpty() ? null : trackData.get(0);
    }

    public Long checkIfExist(Long trackid, Long volume, Long delay)
    {
        StringBuilder query = new StringBuilder("SELECT id FROM ")
            .append(SceneTrackDbModel.TABLE_NAME.value())
            .append(" WHERE trackId = '")
            .append(trackid)
            .append("' AND volume = '")
            .append(volume)
            .append("' AND delay = '")
            .append(delay)
            .append("'");
        ArrayList<Map<String, Long>> trackData = getTrackConfig(dbRead.rawQuery(query.toString(), null));
        return trackData.isEmpty() ? -1L : trackData.get(0).get("id");
    }

    private ArrayList<Map<String, Long>> getTrackConfig(Cursor query)
    {
        ArrayList<Map<String, Long>> result = new ArrayList<>();
        Map<String, Long> content = new HashMap<>();
        Long value;
        while (query.moveToNext())
        {
            for (String attr : SceneTrackDbModel.getValues())
            {
                if (!attr.equals(SceneTrackDbModel.TABLE_NAME.value()))
                {
                    value = query.getColumnIndex(attr) != -1L ? query.getLong(query.getColumnIndex(attr)) : -1L;
                    content.put(attr, value);
                }
            }
            result.add(content);
        }
        return result;
    }
}
