package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.HueBridgeDbModel;
import com.wintermute.gmassistant.database.model.HueBulbDbModel;

import java.util.HashMap;
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

    public Map<String, String> get()
    {
        StringBuilder query =
            new StringBuilder("SELECT ip, username FROM ").append(HueBridgeDbModel.TABLE_NAME.value());
        return getHueConnection(dbRead.rawQuery(query.toString(), null));
    }

    private Map<String, String> getHueConnection(Cursor query)
    {
        Map<String, String> result = new HashMap<>();
        while (query.moveToNext())
        {
            for (String attr : HueBridgeDbModel.getValues())
            {
                if (query.getColumnIndex(attr) != -1)
                {
                    result.put(attr, query.getString(query.getColumnIndex(attr)));
                }
            }
        }
        return result;
    }
}
