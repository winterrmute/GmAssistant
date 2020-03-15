package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.model.SceneDbModel;
import com.wintermute.gmassistant.database.model.Tags;
import com.wintermute.gmassistant.database.model.TrackConfigDbModel;
import com.wintermute.gmassistant.handlers.PlayerHandler;
import com.wintermute.gmassistant.view.model.Light;
import com.wintermute.gmassistant.view.model.Scene;
import com.wintermute.gmassistant.view.model.Track;

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

    /**
     * Creates an instance
     *
     * @param ctx application context
     */
    public SceneOperations(Context ctx)
    {
        this.ctx = ctx;
        dao = new SceneDao(ctx);
    }

    /**
     * Triggers deleting scene
     *
     * @param scene to remove from database
     */
    public void deleteElement(Scene scene)
    {
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
    public Scene getScene(Map<String, Object> content)
    {
        Scene scene = new Scene();
        scene.setId((Long) content.get(SceneDbModel.ID.value()));
        scene.setName((String) content.get(SceneDbModel.NAME.value()));
        scene.setBoardId((Long) content.get(SceneDbModel.BOARD_ID.value()));
        content.remove(SceneDbModel.TABLE_NAME.value());
        content.remove(SceneDbModel.ID.value());
        content.remove(SceneDbModel.NAME.value());
        content.remove(SceneDbModel.BOARD_ID.value());

        for (Map.Entry<String, Object> entry : content.entrySet())
        {
            if (null != entry.getValue())
            {
                if ((Long) entry.getValue() != -1L)
                {
                    if (entry.getKey().equals(SceneDbModel.LIGHT.value()))
                    {
                        LightOperations lightOperations = new LightOperations(ctx);
                        Light light = lightOperations.getLight((Long) entry.getValue());
                        scene.setLight(light);
                    }
                    if (entry.getKey().equals(SceneDbModel.EFFECT.value()))
                    {
                        scene.setEffect(addTrackToScene(scene, (Long) entry.getValue(), Tags.EFFECT.value()));
                    }
                    if (entry.getKey().equals(SceneDbModel.MUSIC.value()))
                    {
                        scene.setMusic(addTrackToScene(scene, (Long) entry.getValue(), Tags.MUSIC.value()));
                    }
                    if (entry.getKey().equals(SceneDbModel.AMBIENCE.value()))
                    {
                        scene.setAmbience(addTrackToScene(scene, (Long) entry.getValue(), Tags.AMBIENCE.value()));
                    }
                }
            }
        }
        return scene;
    }

    /**
     * @param boardId of selected board
     * @return scenes assigned to selected board
     */
    public List<Scene> getScenesAssignedToBoard(Long boardId){
        List<Map<String, Object>> scenesAssignedToBoard = dao.getScenesAssignedToBoard(boardId);
        List<Scene> result = new ArrayList<>();
        for (Map<String, Object> sceneId : scenesAssignedToBoard){
            result.add(getScene(dao.getById((Long) sceneId.get("id"))));

        }
        return result;
    }

    private Track addTrackToScene(Scene scene, Long trackId, String tag)
    {
        TrackOperations trackOperations = new TrackOperations(ctx);
        TrackConfigOperations trackConfig = new TrackConfigOperations(ctx);
        Track track = trackOperations.get(trackId);
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
        new PlayerHandler(ctx).playScene(scene);
    }

    public void createScene(Map<String, Object> sceneContent)
    {
        Scene scene = new Scene();
        ContentValues dbData = new ContentValues();
        for (String entry : sceneContent.keySet())
        {
            if (entry.equals(SceneDbModel.NAME.value()))
            {
                scene.setName(sceneContent.get(SceneDbModel.NAME.value()).toString());
                dbData.put(SceneDbModel.NAME.value(), scene.getName());
                continue;
            }
            if (entry.equals(SceneDbModel.LIGHT.value()))
            {
                scene.setLight((Light) sceneContent.get(SceneDbModel.LIGHT.value()));
                dbData.put(SceneDbModel.LIGHT.value(), scene.getLight().getId());
                continue;
            }
            if (entry.equals(SceneDbModel.EFFECT.value()))
            {
                scene.setEffect((Track) sceneContent.get(SceneDbModel.EFFECT.value()));
                dbData.put(SceneDbModel.EFFECT.value(), scene.getEffect().getId());
                continue;
            }
            if (entry.equals(SceneDbModel.MUSIC.value()))
            {
                scene.setMusic((Track) sceneContent.get(SceneDbModel.MUSIC.value()));
                dbData.put(SceneDbModel.MUSIC.value(), scene.getMusic().getId());
                continue;
            }
            if (entry.equals(SceneDbModel.AMBIENCE.value()))
            {
                scene.setAmbience((Track) sceneContent.get(SceneDbModel.AMBIENCE.value()));
                dbData.put(SceneDbModel.AMBIENCE.value(), scene.getAmbience().getId());
                continue;
            }
            if (entry.equals(SceneDbModel.BOARD_ID.value()))
            {
                scene.setBoardId((Long) sceneContent.get(SceneDbModel.BOARD_ID.value()));
                dbData.put(SceneDbModel.BOARD_ID.value(), scene.getBoardId());
            }
        }
        scene.setId(dao.insert(dbData));

        if (scene.getEffect() != null && scene.getMusic() != null && scene.getAmbience() != null){
            List<Track> tracks = Arrays.asList(scene.getEffect(), scene.getMusic(), scene.getAmbience());
            storeTrackConfig(scene, tracks);
        }
        if (scene.getLight() != null)
        {
            assignLight(scene.getLight().getId(), scene.getId());
        }
    }

    private void assignLight(Long lightId, Long sceneId)
    {
        LightOperations operations = new LightOperations(ctx);
        operations.assignLightToScene(lightId, sceneId);
    }

    private void storeTrackConfig(Scene scene, List<Track> tracks)
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
