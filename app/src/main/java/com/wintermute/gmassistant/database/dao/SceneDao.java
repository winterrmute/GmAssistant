package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.helper.SceneDb;
import com.wintermute.gmassistant.model.Scene;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents Scene data access object.
 *
 * @author wintermute
 */
public class SceneDao extends BaseDao
{

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
    public Long insert(ContentValues scene)
    {
        Long id = null;
        try
        {
            id = dbWrite.insert(SceneDb.TABLE_NAME.value(), null, scene);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return id == null ? -1L : id;
    }

    /**
     * @return list of scenes.
     */
    public List<Map<String, Object>> getAll()
    {
        String query = "SELECT * FROM " + SceneDb.TABLE_NAME.value();
        return createSceneModel(dbRead.rawQuery(query, null));
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
            "SELECT * FROM " + SceneDb.TABLE_NAME.value() + " WHERE " + SceneDb.ID.value() + " = '" + sceneId + "'";
        List<Map<String, Object>> scenes = createSceneModel(dbRead.rawQuery(query, null));
        return scenes.size() != 0 ? scenes.get(0) : null;
    }

    /**
     * Translates the data from database to java objects.
     *
     * @param cursor to iterate over database rows.
     * @return list of track objects.
     */
    private List<Map<String, Object>> createSceneModel(Cursor cursor)
    {
        ArrayList<Map<String, Object>> result = new ArrayList<>();

        while (cursor.moveToNext())
        {
            Map<String, Object> content = new HashMap<>();
            for (SceneDb element : SceneDb.values())
            {
                content.put(element.name().toLowerCase(), getKeyValue(cursor, element.value()));
            }
            result.add(content);
        }
        return result;
    }

    public void updateScene(Scene scene)
    {
        dbWrite.update(SceneDb.TABLE_NAME.value(), null, SceneDb.ID.value() + " = " + scene.getId(), new String[] {});
    }

    /**
     * Safely gets data from database.
     *
     * @param cursor to pick data from database
     * @param key containing value
     * @return value stored in db if possible, otherwise "-1"
     */
    public String getKeyValue(Cursor cursor, String key)
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
     * @param target scene to remove.
     */
    public void delete(Scene target)
    {
        dbWrite.delete(SceneDb.TABLE_NAME.value(), SceneDb.ID.value() + " = " + target.getId(), new String[] {});
    }
}
