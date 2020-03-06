package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.SceneTrackDao;
import com.wintermute.gmassistant.helper.SceneTrackDbModel;
import com.wintermute.gmassistant.model.Track;

public class SceneTrackOperations
{
    private SceneTrackDao dao;

    public SceneTrackOperations(Context ctx)
    {
        dao = new SceneTrackDao(ctx);
    }

    public Long storeTrackWithConfig(Track track)
    {
        Long trackId = dao.checkIfExist(track.getId(), track.getVolume(), track.getDelay());
        if (trackId != -1)
        {
            return trackId;
        } else {
            ContentValues trackConfig = new ContentValues();
            trackConfig.put(SceneTrackDbModel.TRACK_ID.value(), track.getId());
            trackConfig.put(SceneTrackDbModel.VOLUME.value(), track.getVolume());
            trackConfig.put(SceneTrackDbModel.DELAY.value(), track.getDelay());
            return dao.insert(trackConfig);
        }
    }
}
