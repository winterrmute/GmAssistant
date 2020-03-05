package com.wintermute.gmassistant.services.player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.services.notifications.AmbienceReceiver;

/**
 * Handles the ambient sound player.
 *
 * @author wintermute
 */
public class AmbiencePlayer extends BasePlayer implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener
{
    private static final String CHANNEL_ID = "Ambience";
    private MediaPlayer mediaPlayer;
    private PlayerOperations player;

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
        mediaPlayer.start();
    }

    @Override
    public void onCreate()
    {
        mediaPlayer = new MediaPlayer();
        player = PlayerOperations.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Scene scene = intent.getParcelableExtra("scene");
        Track track = scene.getAmbience();
        startForeground(3, createNotification(intent, "Ambience", CHANNEL_ID, AmbienceReceiver.class));
        player.startAmbience(this, track);

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        mediaPlayer.stop();
    }
}
