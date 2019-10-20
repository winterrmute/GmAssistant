package com.wintermute.gmassistant.services.player;

import static android.media.MediaPlayer.create;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.gmassistant.database.dao.SceneDao;

public class EffectPlayerService extends BasePlayerService
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
        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.start();
        if (sceneId != null)
        {
            stopService(new Intent(getBaseContext(), MusicPlayerService.class));
            stopService(new Intent(getBaseContext(), AmbiencePlayerService.class));
            changeLight(sceneId);
            String music = getMusic(sceneId);
            if (music != null)
            {
                mediaPlayer.setOnCompletionListener(mp ->
                {
                    Intent musicService = new Intent(getBaseContext(), MusicPlayerService.class);
                    musicService
                        .putExtra("trackId", getMusic(sceneId))
                        .putExtra("sceneId", sceneId);
                    startService(musicService);
                });
            }
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {

    }

    @Override
    public void onDestroy(){
        mediaPlayer.stop();
    }
}
