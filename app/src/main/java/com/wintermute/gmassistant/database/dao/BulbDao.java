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

public class BulbDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public BulbDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    public Long insert(ContentValues values)
    {
        return dbWrite.insert(HueBulbDbModel.TABLE_NAME.value(), null, values);
    }

    public List<Map<String, String>> getByBridge(HueBridge bridge)
    {
        StringBuilder query = new StringBuilder("SELECT name, type FROM ")
            .append(HueBulbDbModel.TABLE_NAME.value())
            .append(" WHERE ")
            .append(HueBulbDbModel.BRIDGE.value())
            .append(" = '")
            .append(bridge.getId())
            .append("'");
        return getHueConnection(dbRead.rawQuery(query.toString(), null));
    }

    private List<Map<String, String>> getHueConnection(Cursor query)
    {
        List<Map<String, String>> result = new ArrayList<>();
        Map<String, String> bulb = new HashMap<>();
        while (query.moveToNext())
        {
            for (String attr : HueBridgeDbModel.getValues())
            {
                if (query.getColumnIndex(attr) != -1)
                {
                    bulb.put(attr, query.getString(query.getColumnIndex(attr)));
                }
            }
            result.add(bulb);
        }
        return result;
    }
}
