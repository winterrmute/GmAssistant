package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.TrackConfigDao;
import com.wintermute.gmassistant.database.model.TrackConfigDbModel;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;

import java.util.Map;

public class TrackConfigOperations
{
    private TrackConfigDao dao;

    TrackConfigOperations(Context ctx)
    {
        dao = new TrackConfigDao(ctx);
    }

    Map<String, Long> getConfig(Long sceneId, Long trackId)
    {
        return dao.get(sceneId, trackId);
    }

    void storeTrackWithConfig(Long sceneId, Track track)
    {
        ContentValues trackConfig = new ContentValues();
        trackConfig.put(TrackConfigDbModel.SCENE_ID.value(), sceneId);
        trackConfig.put(TrackConfigDbModel.TRACK_ID.value(), track.getId());
        trackConfig.put(TrackConfigDbModel.VOLUME.value(), track.getVolume());
        trackConfig.put(TrackConfigDbModel.DELAY.value(), track.getDelay());
        dao.insert(trackConfig);
    }

    void deleteConfigs(Scene scene)
    {
        dao.delete(scene);
    }
}
