package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.LightConfigDbModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightConfigDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public LightConfigDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    public Long insert(ContentValues values)
    {
        return dbWrite.insert(LightConfigDbModel.TABLE_NAME.value(), null, values);
    }

    public Map<String, Object> get(String ip)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(LightConfigDbModel.TABLE_NAME.value())
            .append(" WHERE ")
            .append(LightConfigDbModel.IP_ADDRESS.value())
            .append(" = '")
            .append(ip)
            .append("'");
        return getLightConfig(dbRead.rawQuery(query.toString(), null)).get(0);
    }

    private List<Map<String, Object>> getLightConfig(Cursor query)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> content;
        while (query.moveToNext())
        {
            content = new HashMap<>();
            for (String attr : LightConfigDbModel.getValues())
            {
                if (!attr.equals(LightConfigDbModel.TABLE_NAME.value()))
                {
                    if (LightConfigDbModel.ID.value().equals(attr))
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
}
