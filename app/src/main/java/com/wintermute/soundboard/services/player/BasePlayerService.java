package com.wintermute.soundboard.services.player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.soundboard.database.dao.LightDao;
import com.wintermute.soundboard.database.dao.SceneDao;
import com.wintermute.soundboard.database.dao.TrackDao;
import com.wintermute.soundboard.database.dto.Light;
import com.wintermute.soundboard.database.dto.Scene;
import com.wintermute.soundboard.handler.LightHandler;

public class BasePlayerService extends Service
{
    String sceneId;
    String trackId;

    /**
     * Changes the light.
     *
     * @param sceneId
     */
    void changeLight(String sceneId)
    {
        if (sceneId != null)
        {
            LightDao dao = new LightDao(getBaseContext());
            SceneDao sdao = new SceneDao(this);
            Scene scene = sdao.getById(sceneId);
            Light light = dao.getById(scene.getLight());
            LightHandler handler = new LightHandler(getBaseContext(), light);
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
                String nextTrackId = getNextTrack(sceneId);
                intent.putExtra("trackId", nextTrackId);
                startService(intent);
            });
        }
    }

    /**
     * @param sceneId to get next track.
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
