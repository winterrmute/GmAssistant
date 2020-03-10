package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.SceneDbModel;
import com.wintermute.gmassistant.database.model.TrackDbModel;
import com.wintermute.gmassistant.view.model.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents Scene data access object.
 *
 * @author wintermute
 */
public class SceneDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public SceneDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
        dbWrite.execSQL("PRAGMA foreign_keys=ON;");
    }

    /**
     * Insert row into scene table.
     *
     * @return id of inserted element.
     */
    public Long insert(ContentValues values)
    {
        return dbWrite.insert(SceneDbModel.TABLE_NAME.value(), null, values);
    }

    /**
     * @return list of scenes.
     */
    public List<Map<String, Object>> getAll()
    {
        String query = "SELECT * FROM " + SceneDbModel.TABLE_NAME.value();
        return getSceneData(dbRead.rawQuery(query, null));
    }

    /**
     * TODO: getById() should move to get()
     *
     * @param sceneId to get next track.
     * @return
     */
    public Map<String, Object> getById(Long sceneId)
    {
        String query =
            "SELECT * FROM " + SceneDbModel.TABLE_NAME.value() + " WHERE " + SceneDbModel.ID.value() + " = '" + sceneId
                + "'";
        List<Map<String, Object>> scenes = getSceneData(dbRead.rawQuery(query, null));
        return scenes.size() != 0 ? scenes.get(0) : null;
    }

    /**
     * Translates the data from database to java objects.
     *
     * @param query to iterate over database rows.
     * @return list of track objects.
     */
    private List<Map<String, Object>> getSceneData(Cursor query)
    {
        ArrayList<Map<String, Object>> result = new ArrayList<>();
        while (query.moveToNext())
        {
            Map<String, Object> content = new HashMap<>();
            for (String attr : SceneDbModel.getValues())
            {
                if (!attr.equals(TrackDbModel.TABLE_NAME.value()))
                {
                    if (attr.equals(SceneDbModel.NAME.value()))
                    {
                        content.put(attr, getStringValue(query, attr));
                    } else
                    {
                        content.put(attr, getNumericalValue(query, attr));
                    }
                }
            }
            result.add(content);
        }
        return result;
    }

    public void updateScene(Scene scene)
    {
        dbWrite.update(SceneDbModel.TABLE_NAME.value(), null, SceneDbModel.ID.value() + " = " + scene.getId(),
            new String[] {});
    }

    private Long getNumericalValue(Cursor query, String attr)
    {

        if (query.getColumnIndex(attr) != -1)
        {
            return query.getLong(query.getColumnIndex(attr)) != 0 ? query.getLong(query.getColumnIndex(attr)) : -1L;
        }
        return -1L;
    }

    /**
     * Safely gets data from database.
     *
     * @param query to pick data from database
     * @param key containing value
     * @return value stored in db if possible, otherwise "-1"
     */
    private String getStringValue(Cursor query, String key)
    {
        if (query.getColumnIndex(key) != -1)
        {
            return query.getString(query.getColumnIndex(key));
        }
        return "-1";
    }

    /**
     * Deletes row from database by id.
     *
     * @param target scene to remove.
     */
    public void delete(Scene target)
    {
        dbWrite.delete(SceneDbModel.TABLE_NAME.value(), SceneDbModel.ID.value() + " = " + target.getId(),
            new String[] {});
    }
}
