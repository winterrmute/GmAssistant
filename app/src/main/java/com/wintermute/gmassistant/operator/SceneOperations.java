package com.wintermute.gmassistant.operator;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.handlers.PlayerHandler;
import com.wintermute.gmassistant.helper.SceneDb;
import com.wintermute.gmassistant.model.Light;
import com.wintermute.gmassistant.model.Scene;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for informing views that data has changed, doing basic operations on models and receiving
 * events from views.
 *
 * @author wintermute
 */
public class SceneOperations
{
    private Context sceneAppContext;
    private SceneDao dao;

    //TODO: Tmp
    private Scene scene;
    private List<Scene> allScenes = new ArrayList<>();

    public SceneOperations(Context ctx)
    {
        this.sceneAppContext = ctx;
    }

    public List<Scene> loadViewElements()
    {
        dao = new SceneDao(sceneAppContext);
        List<Map<String, Object>> actualElements = dao.getAll();
        if (actualElements.size() > 0)
        {
            Scene scene;
            for (Map<String, Object> sceneContent : actualElements)
            {
                scene = getModel(sceneContent);
                allScenes.add(scene);
            }
            return allScenes;
        }
        return new ArrayList<>();
    }

    public Scene getScene(Scene scene)
    {
        dao = new SceneDao(sceneAppContext);
        return getModel(dao.getById(scene.getId()));
    }

    public Scene getScene(Long sceneId)
    {
        dao = new SceneDao(sceneAppContext);
        return getModel(dao.getById(sceneId));
    }

    public void createInstance(Map<String, Object> content)
    {
        scene = new Scene();
        storeScene(content);
    }

    public void deleteElement(Scene scene)
    {
        dao = new SceneDao(sceneAppContext);
        dao.delete(scene);
        allScenes = loadViewElements();
    }

    private Scene getModel(Map<String, Object> content)
    {
        if (scene == null)
        {
            scene = new Scene();
        }
        for (Map.Entry<String, Object> entry : content.entrySet())
        {
            if (null != entry.getValue())
            {
                if (entry.getKey().equals(SceneDb.ID.value()))
                {
                    scene.setId(Long.parseLong(entry.getValue().toString()));
                }
                if (entry.getKey().equals(SceneDb.LIGHT.value()))
                {
                    Light light = (Light) entry.getValue();
                    scene.setLight(light);
                }
                if (entry.getKey().equals(SceneDb.NAME.value()))
                {
                    scene.setName(entry.getValue().toString());
                }
                if (entry.getKey().equals(SceneDb.EFFECT.value()))
                {
                    scene.setEffectPath(entry.getValue().toString());
                }
                if (entry.getKey().equals(SceneDb.MUSIC.value()))
                {
                    scene.setMusicPath(entry.getValue().toString());
                }
                if (entry.getKey().equals(SceneDb.AMBIENCE.value()))
                {
                    scene.setAmbiencePath(entry.getValue().toString());
                }
            }
        }
        return scene;
    }

    public void startScene(Long id)
    {
        PlayerHandler handler = new PlayerHandler(sceneAppContext);
        handler.startPlayerByScene(id);
    }

    private void storeScene(Map<String, Object> sceneContent)
    {
        ContentValues result = new ContentValues();
        for (Map.Entry<String, Object> entry : sceneContent.entrySet())
        {
            if (null != entry.getValue())
            if (entry.getKey().equals(SceneDb.LIGHT.value()))
            {
                Light light = (Light) entry.getValue();
                Long value = light.getId();
                result.put(entry.getKey(), value);
            } else
            {
                String value = entry.getValue().toString();
                result.put(entry.getKey(), value);
                result.put(entry.getKey(), value);
            }
        }
        SceneDao dao = new SceneDao(sceneAppContext);
        Long insert = dao.insert(result);
        scene.setId(insert);
    }
}
