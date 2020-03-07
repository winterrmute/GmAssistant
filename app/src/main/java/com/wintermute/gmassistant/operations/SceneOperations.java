package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.handlers.PlayerHandler;
import com.wintermute.gmassistant.database.model.SceneDbModel;
import com.wintermute.gmassistant.database.model.Tags;
import com.wintermute.gmassistant.database.model.TrackConfigDbModel;
import com.wintermute.gmassistant.model.Light;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;

import java.util.ArrayList;
import java.util.Arrays;
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
        List<Scene> result = new ArrayList<>();
        List<Map<String, Object>> foundScenes = dao.getAll();
        if (foundScenes.size() > 0)
        {
            for (Map<String, Object> sceneContent : foundScenes)
            {
                result.add(getModel(sceneContent));
            }
        }
        return result;
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
        TrackConfigOperations operation = new TrackConfigOperations(ctx);
        operation.deleteConfigs(scene);
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
        scene.setId((Long) content.get(SceneDbModel.ID.value()));
        scene.setName((String) content.get(SceneDbModel.NAME.value()));
        content.remove(SceneDbModel.TABLE_NAME.value());
        content.remove(SceneDbModel.NAME.value());
        content.remove(SceneDbModel.ID.value());

        for (Map.Entry<String, Object> entry : content.entrySet())
        {
            if (null != entry.getValue())
            {
                if ((Long) entry.getValue() != -1L)
                {
                    if (entry.getKey().equals(SceneDbModel.LIGHT.value()))
                    {
                        scene.setLight((Light) entry.getValue());
                    }
                    if (entry.getKey().equals(SceneDbModel.EFFECT.value()))
                    {
                        scene.setEffect(addTrackToScene((Long) entry.getValue(), Tags.EFFECT.value()));
                    }
                    if (entry.getKey().equals(SceneDbModel.MUSIC.value()))
                    {
                        scene.setMusic(addTrackToScene((Long) entry.getValue(), Tags.MUSIC.value()));
                    }
                    if (entry.getKey().equals(SceneDbModel.AMBIENCE.value()))
                    {
                        scene.setAmbience(addTrackToScene((Long) entry.getValue(), Tags.AMBIENCE.value()));
                    }
                }
            }
        }
        return scene;
    }

    private Track addTrackToScene(Long id, String tag)
    {
        TrackOperations trackOperations = new TrackOperations(ctx);
        TrackConfigOperations trackConfig = new TrackConfigOperations(ctx);
        Track track = trackOperations.get(id);
        track.setTag(tag);
        Map<String, Long> config = trackConfig.getConfig(scene.getId(), track.getId());
        if (config != null)
        {
            track.setVolume(config.get(TrackConfigDbModel.VOLUME.value()));
            track.setDelay(config.get(TrackConfigDbModel.DELAY.value()));
        }
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
                    result.put(entry.getKey(), track.getId());
                } else
                {
                    String value = entry.getValue().toString();
                    result.put(entry.getKey(), value);
                }
            } else
            {
                result.put(entry.getKey(), -1);
            }
        }
        SceneDao dao = new SceneDao(ctx);
        Long id = dao.insert(result);
        scene.setId(id);

        List<Track> tracks =
            Arrays.asList((Track) sceneContent.get(Tags.EFFECT.value()), (Track) sceneContent.get(Tags.MUSIC.value()),
                (Track) sceneContent.get(Tags.AMBIENCE.value()));
        storeTrackConfig(tracks);
    }

    void storeTrackConfig(List<Track> tracks)
    {
        TrackConfigOperations operations = new TrackConfigOperations(ctx);
        for (Track track : tracks)
        {
            if (track != null)
            {
                operations.storeTrackWithConfig(scene.getId(), track);
            }
        }
    }
}
