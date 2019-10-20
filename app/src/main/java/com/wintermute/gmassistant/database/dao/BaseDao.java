package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.google.common.collect.Maps;
import com.wintermute.gmassistant.database.dto.Scene;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

abstract class BaseDao
{

    SQLiteDatabase dbRead;
    SQLiteDatabase dbWrite;

    /**
     * @param updateValues map containing changed values.
     * @return query containing changed values.
     */
    StringBuilder updateQueryBuilder(Map<String, String> updateValues)
    {
        updateValues = removeEmptyValues(updateValues);
        StringBuilder query = new StringBuilder();
        Iterator<Map.Entry<String, String>> it = updateValues.entrySet().iterator();
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
     * Delete row from table.
     *
     * @param tableName to deleteById the row from.
     * @param key is the unique element id.
     * @param value of id to remove.
     */
    public void delete(String tableName, String key, String value)
    {
        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(tableName)
            .append(" WHERE ")
            .append(key)
            .append(" = '")
            .append(value)
            .append("'");
        dbWrite.execSQL(query.toString());
    }
}
