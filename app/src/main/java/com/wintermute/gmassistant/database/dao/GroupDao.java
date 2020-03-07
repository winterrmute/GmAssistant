package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.GroupDbModel;
import com.wintermute.gmassistant.database.model.TrackDbModel;

import java.util.HashMap;
import java.util.Map;

public class GroupDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public GroupDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    /**
     * Get track details.
     *
     * @param id of group.
     * @return selected track.
     */
    public Map<Long, String> get(Long id)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(GroupDbModel.TABLE_NAME.value())
            .append("  WHERE id = '")
            .append(id)
            .append("'");
        return getGroupData(dbRead.rawQuery(query.toString(), null));
    }

    private Map<Long, String> getGroupData(Cursor query)
    {
        Map<Long, String> groups = new HashMap<>();
        while (query.moveToNext())
        {
            Long id = query.getLong(query.getColumnIndex("id"));
            String name = query.getString(query.getColumnIndex("name"));
            groups.put(id, name);
        }
        return groups;
    }

    public Long createGroup(ContentValues values) {
        return dbWrite.insert(GroupDbModel.TABLE_NAME.value(), null, values);
    }
}
