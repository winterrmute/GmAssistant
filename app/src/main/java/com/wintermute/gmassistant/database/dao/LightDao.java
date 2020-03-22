package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.LightDbModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents Light Scene data access object.
 *
 * @author wintermute
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LightDao extends BaseDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public LightDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
        dbWrite.execSQL("PRAGMA foreign_keys=ON;");
    }

    /**
     * Create new light scene.
     *
     * @return id.
     */
    public Long insert(ContentValues values)
    {
        return dbWrite.insert(LightDbModel.TABLE_NAME.value(), null, values);
    }

    public Map<String, Object> get(Long lightId)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(LightDbModel.TABLE_NAME.value())
            .append(" WHERE id = '")
            .append(lightId)
            .append("'");
        return getLightData(dbRead.rawQuery(query.toString(), null));
    }

    private Map<String, Object> getLightData(Cursor query)
    {
        Map<String, Object> result = new HashMap<>();
        while (query.moveToNext())
        {
            result = new HashMap<>();
            for (String attr : LightDbModel.getValues())
            {
                if (query.getColumnIndex(attr) != -1)
                {
                    result.put(attr, query.getLong(query.getColumnIndex(attr)));
                }
            }
        }
        return result;
    }

    public void update(Long lightId, ContentValues values)
    {
        dbWrite.update(LightDbModel.TABLE_NAME.value(), values, LightDbModel.ID.value() + " = " + lightId,
            new String[] {});
    }
}
