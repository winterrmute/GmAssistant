package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.HueBridgeDbModel;

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
        return dbWrite.insertWithOnConflict(HueBridgeDbModel.TABLE_NAME.value(), null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Map<String, String> get(String ip)
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

    public List<Map<String, String>> getAll()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ").append(HueBridgeDbModel.TABLE_NAME.value());
        return getHueBridges(dbRead.rawQuery(query.toString(), null));
    }

    private List<Map<String, String>> getHueBridges(Cursor query)
    {
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> bridge = new HashMap<>();
        while (query.moveToNext())
        {
            for (String attr : HueBridgeDbModel.getValues())
            {
                if (query.getColumnIndex(attr) != -1)
                {
                    bridge.put(attr, query.getString(query.getColumnIndex(attr)));
                }
            }
            result.add(bridge);
        }
        return result;
    }
}
