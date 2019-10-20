package com.wintermute.gmassistant.services.player;

import static android.media.MediaPlayer.create;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dto.Scene;

/**
 * Handles the background music player.
 *
 * @author wintermute
 */
public class MusicPlayerService extends BasePlayerService
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp) { }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra)
    {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp)
    {
        mp.start();
    }

    @Override
    public void onCreate()
    {
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        getExtras(intent);
        mediaPlayer.stop();
        mediaPlayer = create(this, Uri.parse(getTrackPath(trackId)));
        mediaPlayer.setVolume(0.2f, 0.2f);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        if (sceneId != null)
        {
            changeLight(sceneId);
            String ambience = getAmbience(sceneId);
            if (null != ambience) {
                Intent backgroundService = new Intent(getBaseContext(), AmbiencePlayerService.class);
                backgroundService.putExtra("trackId", ambience);
                startService(backgroundService);
            }
        }

        return Service.START_NOT_STICKY;
    }
}
