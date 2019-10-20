package com.wintermute.gmassistant.services.player;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.gmassistant.database.dao.LightDao;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Light;
import com.wintermute.gmassistant.handlers.LightHandler;

import java.math.BigDecimal;

public class BasePlayerService extends Service
{
    String sceneId;
    String trackId;
    String playlistId;

    /**
     * Changes the light.
     *
     * @param sceneId
     */
    void changeLight(String sceneId)
    {
        SceneDao sceneDao = new SceneDao(this);
        String lightId = sceneDao.getById(sceneId).getLight();
        if (!"null".equals(lightId) && null != lightId)
        {
            LightDao dao = new LightDao(getBaseContext());
            Light light = dao.getById(lightId);
            Color color = Color.valueOf(new BigDecimal(String.valueOf(light.getColor())).intValue());
            LightHandler handler = new LightHandler(getBaseContext(), color, Integer.parseInt(light.getBrightness()));
            handler.setLight(false);
        }
    }

    /**
     * Configures the media player.
     *
     * @param intent to get extras from.
     */
    void getExtras(Intent intent)
    {
        sceneId = intent.getStringExtra("sceneId");
        playlistId = intent.getStringExtra("playlistId");
        trackId = intent.getStringExtra("trackId");
    }

    /**
     * @return next track to play.
     */
    String getMusic(String sceneId)
    {
        SceneDao dao = new SceneDao(getBaseContext());
        return dao.getById(sceneId).getBackgroundMusic();
    }

    String getAmbience(String sceneId)
    {
        SceneDao dao = new SceneDao(getBaseContext());
        return dao.getById(sceneId).getBackgroundAmbience();
    }

    /**
     * @param trackId to extract the path.
     * @return path of track
     */
    String getTrackPath(String trackId)
    {
        TrackDao dao = new TrackDao(getBaseContext());
        return dao.computeTrackIfAbsent(trackId).getPath();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
