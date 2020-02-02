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
    StringBuilder updateQueryBuilder(Map<String, Object> dbQueryParams)
    {
        dbQueryParams = Maps.filterValues(dbQueryParams, Objects::nonNull);
        StringBuilder query = new StringBuilder();
        Iterator<Map.Entry<String, Object>> it = dbQueryParams.entrySet().iterator();
        while (it.hasNext())
        {
            Map.Entry<String, Object> entry = it.next();

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
    Map<String, Object> removeEmptyValues(Map<String, Object> object)
    {
        return Maps.filterValues(object, Objects::nonNull);
    }

    /**
     * @param object to translate into values
     * @return values to insert into database TODO: optimize check fo type especially for ids and exception handling
     */
    ContentValues getContentValues(Map<String, Object> object)
    {
        ContentValues values = new ContentValues();
        for (Map.Entry<String, Object> entry : object.entrySet())
        {
            if (entry.getValue() instanceof String)
            {
                values.put(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Long)
            {
                values.put(entry.getKey(), (Long) entry.getValue());
            } else if (entry.getValue() instanceof Boolean)
            {
                values.put(entry.getKey(), (Boolean) entry.getValue());
            }
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
