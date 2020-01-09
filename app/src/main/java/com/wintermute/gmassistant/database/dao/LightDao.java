package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.dto.Light;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents Light Scene data access object.
 *
 * @author wintermute
 */
@Data
@EqualsAndHashCode(callSuper=false)
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
     * @param light color and brightness information.
     * @return id.
     */
    public long insert(Light light)
    {
        Map<String, String> object = createObject(light);
        ContentValues values = getContentValues(object);
        return dbWrite.insert(TABLE_NAME, null, values);
    }

    public Light getById(String lightId)
    {
        StringBuilder query =
            new StringBuilder("SELECT * FROM ").append(TABLE_NAME).append(" WHERE id = '").append(lightId).append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null)).get(0);
    }

    /**
     * Translates the data from database to java objects.
     *
     * @param cursor to iterate over database rows.
     * @return list of track objects.
     */
    private ArrayList<Light> mapObject(Cursor cursor)
    {
        ArrayList<Light> result = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Light light = new Light();
            light.setId(getKeyValue(cursor, ID_KEY));
            light.setColor(getKeyValue(cursor, COLOR_KEY));
            light.setColor(getKeyValue(cursor, COLOR_KEY));
            light.setBrightness(getKeyValue(cursor, BRIGHTNESS_KEY));
            result.add(light);
        }
        return result;
    }

    /**
     * @return map containing non null values.
     */
    private Map<String, String> createObject(Light target)
    {
        HashMap<String, String> obj = new HashMap<>();
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
}
