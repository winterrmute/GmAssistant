package com.wintermute.soundboard.services.player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.soundboard.database.dao.LightDao;
import com.wintermute.soundboard.database.dao.SceneDao;
import com.wintermute.soundboard.database.dao.TrackDao;
import com.wintermute.soundboard.handler.LightHandler;

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
            LightHandler handler = new LightHandler(getBaseContext(), dao.getById(sceneLight));
            handler.manageLight();
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
