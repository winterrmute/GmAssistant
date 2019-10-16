package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.dto.Scene;

import java.util.ArrayList;

/**
 * Represents Scene data access object.
 *
 * @author wintermute
 */
public class SceneDao
{
    private static final String TABLE_NAME = "scene";
    private static final String ID_KEY = "id";
    private static final String LIGHT_KEY = "light";
    private static final String NEXT_TRACK_KEY = "next_track";

    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public SceneDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    /**
     * Insert row into scene table.
     *
     * @return id of inserted element.
     */
    public long insert(Scene scene)
    {
        ContentValues values = new ContentValues();
        values.put(LIGHT_KEY, scene.getLight());
        values.put(NEXT_TRACK_KEY, scene.getNextTrack());
        return dbWrite.insert(TABLE_NAME, null, values);
    }

    /**
     * TODO: Refactor this shit.
     *
     * @param sceneId to get next track.
     * @return
     */
    public Scene getById(String sceneId)
    {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_KEY + " = '" + sceneId + "'";
        return mapObject(dbRead.rawQuery(query, null)).get(0);
    }

    /**
     * Translates the data from database to java objects.
     *
     * @param cursor to iterate over database rows.
     * @return list of track objects.
     */
    private ArrayList<Scene> mapObject(Cursor cursor)
    {
        ArrayList<Scene> result = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Scene scene = new Scene();
            scene.setLight(getKeyValue(cursor, LIGHT_KEY));
            scene.setNextTrack(getKeyValue(cursor, NEXT_TRACK_KEY));
            result.add(scene);
        }
        return result;
    }

    /**
     * Safely gets data from database.
     *
     * @param cursor to pick data from database
     * @param key containing value
     * @return value stored in db if possible, otherwise "-1"
     */
    private String getKeyValue(Cursor cursor, String key)
    {
        if (cursor.getColumnIndex(key) != -1)
        {
            return cursor.getString(cursor.getColumnIndex(key));
        }
        return "-1";
    }
}