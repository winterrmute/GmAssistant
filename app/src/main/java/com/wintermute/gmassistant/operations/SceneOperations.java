package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.handlers.PlayerHandler;
import com.wintermute.gmassistant.helper.SceneDbModel;
import com.wintermute.gmassistant.helper.Tags;
import com.wintermute.gmassistant.model.Light;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;

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
    private Context ctx;
    private SceneDao dao;

    private Scene scene;
    private List<Scene> allScenes = new ArrayList<>();

    /**
     * Creates an instance
     *
     * @param ctx application context
     */
    public SceneOperations(Context ctx)
    {
        this.ctx = ctx;
    }

    /**
     * Gets all scenes stored in database
     *
     * @return list of scenes
     */
    public List<Scene> loadViewElements()
    {
        dao = new SceneDao(ctx);
        List<Map<String, Object>> foundScenes = dao.getAll();
        if (foundScenes.size() > 0)
        {
            for (Map<String, Object> sceneContent : foundScenes)
            {
                allScenes.add(getModel(sceneContent));
            }
            return allScenes;
        }
        return new ArrayList<>();
    }

    /**
     * @param scene to load from database
     * @return scene
     */
    public Scene getScene(Scene scene)
    {
        dao = new SceneDao(ctx);
        return getModel(dao.getById(scene.getId()));
    }

    /**
     * @param sceneId to load from database
     * @return scene by id
     */
    public Scene getScene(Long sceneId)
    {
        dao = new SceneDao(ctx);
        return getModel(dao.getById(sceneId));
    }

    /**
     * Creates new scene
     *
     * @param content all scene information
     */
    public void createScene(Map<String, Object> content)
    {
        scene = new Scene();
        storeScene(content);
    }

    /**
     * Triggers deleting scene
     *
     * @param scene to remove from database
     */
    public void deleteElement(Scene scene)
    {
        dao = new SceneDao(ctx);
        dao.delete(scene);
        allScenes = loadViewElements();
    }

    /**
     * Gets the scene model with all information
     *
     * @param content model data
     * @return composed model
     */
    private Scene getModel(Map<String, Object> content)
    {
        scene = new Scene();
        for (Map.Entry<String, Object> entry : content.entrySet())
        {
            if (null != entry.getValue())
            {
                if (entry.getKey().equals(SceneDbModel.ID.value()))
                {
                    scene.setId(Long.parseLong(entry.getValue().toString()));
                }
                if (entry.getKey().equals(SceneDbModel.LIGHT.value()))
                {
                    scene.setLight((Light) entry.getValue());
                }
                if (entry.getKey().equals(SceneDbModel.NAME.value()))
                {
                    scene.setName(entry.getValue().toString());
                }
                if (entry.getKey().equals(SceneDbModel.EFFECT.value()))
                {
                    scene.setEffect(addTrackToScene(entry.getValue().toString(), Tags.EFFECT.value()));
                }
                if (entry.getKey().equals(SceneDbModel.MUSIC.value()))
                {
                    scene.setMusic(addTrackToScene(entry.getValue().toString(), Tags.MUSIC.value()));
                }
                if (entry.getKey().equals(SceneDbModel.AMBIENCE.value()))
                {
                    scene.setAmbience(addTrackToScene(entry.getValue().toString(), Tags.AMBIENCE.value()));
                }
            }
        }
        return scene;
    }

    private Track addTrackToScene(String id, String tag)
    {
        TrackOperations trackOperations = new TrackOperations(ctx);
        Track track = trackOperations.get(id);
        track.setTag(tag);
        return track;
    }

    /**
     * Triggers playing the scene
     */
    public void startScene(Scene scene)
    {
        new PlayerHandler(ctx).startPlayers(scene);
    }

    private void storeScene(Map<String, Object> sceneContent)
    {
        ContentValues result = new ContentValues();
        for (Map.Entry<String, Object> entry : sceneContent.entrySet())
        {
            if (null != entry.getValue())
            {
                if (entry.getKey().equals(SceneDbModel.LIGHT.value()))
                {
                    Light light = (Light) entry.getValue();
                    Long value = light.getId();
                    result.put(entry.getKey(), value);
                } else if (entry.getKey().equals(Tags.EFFECT.value()) || entry.getKey().equals(Tags.MUSIC.value())
                    || entry.getKey().equals(Tags.AMBIENCE.value()))
                {
                    Track track = (Track) entry.getValue();
                    Long value = track.getId();
                    result.put(entry.getKey(), value);
                } else
                {
                    String value = entry.getValue().toString();
                    result.put(entry.getKey(), value);
                }
            }
        }
        SceneDao dao = new SceneDao(ctx);
        Long insert = dao.insert(result);
        scene.setId(insert);
    }
}
