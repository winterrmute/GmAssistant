package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.HueBridgeDbModel;
import com.wintermute.gmassistant.database.model.HueBulbDbModel;
import com.wintermute.gmassistant.hue.model.HueBridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HueBridgeDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public HueBridgeDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    public Long insert(ContentValues values)
    {
        return dbWrite.insertWithOnConflict(HueBridgeDbModel.TABLE_NAME.value(), null, values,
            SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Map<String, Object> get(String ip)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(HueBridgeDbModel.TABLE_NAME.value())
            .append(" WHERE ")
            .append(HueBridgeDbModel.IP_ADDRESS.value())
            .append(" = ")
            .append(ip)
            .append("'");
        return getHueBridges(dbRead.rawQuery(query.toString(), null)).get(0);
    }

    public Map<String, Object> getActiveBridge()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(HueBridgeDbModel.TABLE_NAME.value())
            .append(" WHERE ")
            .append(HueBridgeDbModel.ACTIVE.value())
            .append(" = 1'");
        return getHueBridges(dbRead.rawQuery(query.toString(), null)).get(0);
    }

    public List<Map<String, Object>> getAll()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ").append(HueBridgeDbModel.TABLE_NAME.value());
        return getHueBridges(dbRead.rawQuery(query.toString(), null));
    }

    private List<Map<String, Object>> getHueBridges(Cursor query)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> bridge = new HashMap<>();
        while (query.moveToNext())
        {
            for (String attr : HueBridgeDbModel.getValues())
            {
                if (attr.equals(HueBulbDbModel.ID.value()))
                {
                    bridge.put(attr, getNumericalValue(query, attr));
                } else
                {
                    bridge.put(attr, getStringValue(query, attr));
                }
            }
            result.add(bridge);
        }
        return result;
    }

    private Long getNumericalValue(Cursor cursor, String column)
    {

        if (cursor.getColumnIndex(column) != -1)
        {
            return cursor.getLong(cursor.getColumnIndex(column));
        }
        return -1L;
    }

    private String getStringValue(Cursor cursor, String column)
    {
        if (cursor.getColumnIndex(column) != -1)
        {
            return cursor.getString(cursor.getColumnIndex(column));
        }
        return "-1";
    }

    public void delete(HueBridge bridge)
    {
        dbWrite.delete(HueBridgeDbModel.TABLE_NAME.value(),
            HueBridgeDbModel.IP_ADDRESS.value() + " = '" + bridge.getIp() + "'", new String[] {});
    }
}
