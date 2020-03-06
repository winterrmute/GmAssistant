package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.TrackConfigDao;
import com.wintermute.gmassistant.helper.SceneTrackDbModel;
import com.wintermute.gmassistant.model.Track;

import java.util.Map;

public class TrackConfigOperations
{
    private TrackConfigDao dao;

    TrackConfigOperations(Context ctx)
    {
        dao = new TrackConfigDao(ctx);
    }

    Map<String, Long> getConfig(Long id)
    {
        return dao.get(id);
    }

    void storeTrackWithConfig(Long sceneId, Track track)
    {
            ContentValues trackConfig = new ContentValues();
            trackConfig.put(SceneTrackDbModel.SCENE_ID.value(), sceneId);
            trackConfig.put(SceneTrackDbModel.TRACK_ID.value(), track.getId());
            trackConfig.put(SceneTrackDbModel.VOLUME.value(), track.getVolume());
            trackConfig.put(SceneTrackDbModel.DELAY.value(), track.getDelay());
            dao.insert(trackConfig);
    }
}
