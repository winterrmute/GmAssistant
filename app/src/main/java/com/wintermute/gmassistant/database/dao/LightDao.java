package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.LightDbModel;
import com.wintermute.gmassistant.view.model.Light;
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
    private static final String TABLE_NAME = "light";
    private static final String ID_KEY = "id";
    private static final String COLOR_KEY = "color";
    private static final String BRIGHTNESS_KEY = "brightness";

    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public LightDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    /**
     * Create new light scene.
     *
     * @return id.
     */
    public long insert(ContentValues values)
    {
        return dbWrite.insert(TABLE_NAME, null, values);
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
                    if (attr.equals(LightDbModel.COLOR.value()))
                    {
                        result.put(attr, query.getLong(query.getColumnIndex(attr)));
                    } else
                    {
                        result.put(attr, query.getLong(query.getColumnIndex(attr)));
                    }
                }
            }
        }
        return result;
    }

    /**
     * @return map containing non null values.
     */
    private Map<String, Object> createObject(Light target)
    {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put(ID_KEY, target.getId());
        obj.put(COLOR_KEY, target.getColor());
        obj.put(BRIGHTNESS_KEY, target.getBrightness());
        return removeEmptyValues(obj);
    }

    /**
     * Safely gets data from database.
     *
     * @param cursor to pick data from database
     * @param column containing value
     * @return value stored in db if possible, otherwise "-1"
     */
    private String getKeyValue(Cursor cursor, String column)
    {
        if (cursor.getColumnIndex(column) != -1)
        {
            return cursor.getString(cursor.getColumnIndex(column));
        }
        return "-1";
    }

    public void delete(Light light)
    {
        dbWrite.delete(LightDbModel.TABLE_NAME.value(), LightDbModel.ID.value() + " = " + light.getId(),
            new String[] {});
    }
}
