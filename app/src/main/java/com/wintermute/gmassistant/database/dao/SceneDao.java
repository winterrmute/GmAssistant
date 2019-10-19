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
    private static final String STARTING_TRACK_KEY = "start_track";
    private static final String NAME_KEY = "name";
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
        values.put(NAME_KEY, scene.getName());
        values.put(STARTING_TRACK_KEY, scene.getStartingTrack());
        values.put(NEXT_TRACK_KEY, scene.getNextTrack());
        return dbWrite.insert(TABLE_NAME, null, values);
    }

    /**
     * @return list of scenes.
     */
    public ArrayList<Scene> getAll()
    {
        String query = "SELECT * FROM " + TABLE_NAME;
        return mapObject(dbRead.rawQuery(query, null));
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
        ArrayList<Scene> scenes = mapObject(dbRead.rawQuery(query, null));
        return scenes.size() != 0 ? scenes.get(0) : null;
    }

    public void updateScene(Scene scene)
    {
        StringBuilder query = new StringBuilder("UPDATE ")
            .append(TABLE_NAME)
            .append(" SET ")
            .append(NAME_KEY)
            .append("= '")
            .append(scene.getName())
            .append("', ")
            .append(LIGHT_KEY)
            .append(" = '")
            .append(scene.getLight())
            .append("', ")
            .append(STARTING_TRACK_KEY)
            .append(" = '")
            .append(scene.getStartingTrack())
            .append("', ")
            .append(NEXT_TRACK_KEY)
            .append(" = '")
            .append(scene.getNextTrack())
            .append("' WHERE ")
            .append(ID_KEY)
            .append(" = '")
            .append(scene.getId())
            .append("'");
        dbWrite.execSQL(query.toString());
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
            scene.setId(getKeyValue(cursor, ID_KEY));
            scene.setName(getKeyValue(cursor, NAME_KEY));
            scene.setLight(getKeyValue(cursor, LIGHT_KEY));
            scene.setStartingTrack(getKeyValue(cursor, STARTING_TRACK_KEY));
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

    /**
     * Deletes row from database by id.
     *
     * @param id of track to remove.
     */
    public void deleteById(String id)
    {
        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(ID_KEY)
            .append(" = '")
            .append(id)
            .append("'");
        dbWrite.execSQL(query.toString());
    }
}
