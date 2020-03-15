package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.HueBulbDbModel;
import com.wintermute.gmassistant.hue.model.HueBridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulbDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public BulbDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
        dbWrite.execSQL("PRAGMA foreign_keys=ON;");
    }

    public Long insert(ContentValues values)
    {
        return dbWrite.insert(HueBulbDbModel.TABLE_NAME.value(), null, values);
    }

    public List<Map<String, Object>> getByBridge(HueBridge bridge)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(HueBulbDbModel.TABLE_NAME.value())
            .append(" WHERE ")
            .append(HueBulbDbModel.BRIDGE.value())
            .append(" = '")
            .append(bridge.getId())
            .append("'");
        return getHueConnection(dbRead.rawQuery(query.toString(), null));
    }

    private List<Map<String, Object>> getHueConnection(Cursor query)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> bulb;
        while (query.moveToNext())
        {
            bulb = new HashMap<>();
            for (String attr : HueBulbDbModel.getValues())
            {
                if (attr.equals(HueBulbDbModel.BRIDGE.value()) || attr.equals(HueBulbDbModel.ID.value()))
                {
                    if (query.getColumnIndex(attr) != -1)
                    {
                        bulb.put(attr, query.getLong(query.getColumnIndex(attr)));
                    }
                } else if (!attr.equals(HueBulbDbModel.TABLE_NAME.value()))
                {
                    if (query.getColumnIndex(attr) != -1)
                    {
                        bulb.put(attr, query.getString(query.getColumnIndex(attr)));
                    }
                }
            }
            result.add(bulb);
        }
        dbWrite.close();
        return result;
    }
}
