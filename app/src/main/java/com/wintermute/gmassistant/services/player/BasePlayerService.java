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
import com.wintermute.gmassistant.handler.LightHandler;

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
        if (sceneDao.getById(sceneId).getLight() != null)
        {
            String sceneLight = sceneDao.getById(sceneId).getLight();
            LightDao dao = new LightDao(getBaseContext());
            Light light = dao.getById(sceneLight);
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
        trackId = intent.getStringExtra("trackId");
        playlistId = intent.getStringExtra("playlistId");
    }

    /**
     * Play following music on complete if not null.
     *
     * @param player to set completion listener.
     */
    void playNextOnComplete(MediaPlayer player)
    {
        if (sceneId != null)
        {
            player.setOnCompletionListener(mp ->
            {
                Intent intent = new Intent(getBaseContext(), BackgroundMusic.class);
                SceneDao dao = new SceneDao(getBaseContext());
                intent.putExtra("trackId", dao.getById(sceneId).getNextTrack());
                startService(intent);
            });
        }
    }

    /**
     * @return next track to play.
     */
    String getNextTrack(String sceneId)
    {
        SceneDao dao = new SceneDao(getBaseContext());
        return dao.getById(sceneId).getNextTrack();
    }

    /**
     * @param trackId to extract the path.
     * @return path of track
     */
    String getTrackPath(String trackId)
    {
        TrackDao dao = new TrackDao(getBaseContext());
        return dao.getTrackById(trackId).getPath();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}