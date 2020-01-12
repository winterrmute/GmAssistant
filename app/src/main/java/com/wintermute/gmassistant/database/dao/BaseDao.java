package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.google.common.collect.Maps;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

abstract class BaseDao
{

    SQLiteDatabase dbRead;
    SQLiteDatabase dbWrite;

    /**
     * @param dbQueryParams containing key value pairs to process in db.
     * @return database query.
     */
    StringBuilder updateQueryBuilder(Map<String, String> dbQueryParams)
    {
        dbQueryParams = Maps.filterValues(dbQueryParams, Objects::nonNull);
        StringBuilder query = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = dbQueryParams.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, String> entry = it.next();

            if (entry.getValue() != null)
            {
                query.append(entry.getKey()).append(" = '").append(entry.getValue()).append("'");
            }
            if (it.hasNext())
            {
                query.append(", ");
            }
        }
        return query;
    }

    /**
     * @param object to edit.
     * @return map contining only non-null entries.
     */
    Map<String, String> removeEmptyValues(Map<String, String> object)
    {
        return Maps.filterValues(object, Objects::nonNull);
    }

    /**
     * @param object to translate into values
     * @return values to insert into database
     */
    ContentValues getContentValues(Map<String, String> object)
    {
        ContentValues values = new ContentValues();
        for (Map.Entry<String, String> entry : object.entrySet())
        {
            values.put(entry.getKey(), entry.getValue());
        }
        return values;
    }

    /**
     * @param tableName to delete an item from.
     * @param key is the row name in defined database table.
     * @param value to identify element to delete.
     * @return database delete query by specified key for element as String.
     */
    public String getDeleteQuery(String tableName, String key, String value)
    {
        return new StringBuilder("DELETE FROM ")
            .append(tableName)
            .append(" WHERE ")
            .append(key)
            .append(" = '")
            .append(value)
            .append("'")
            .toString();
    }

    /**
     * Cloeses db con.
     */
    public void close()
    {
        if (null != dbRead)
        {
            dbRead.close();
        }
        if (null != dbWrite)
        {
            dbWrite.close();
        }
    }
}
