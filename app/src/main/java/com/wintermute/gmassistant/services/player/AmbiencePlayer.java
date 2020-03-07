package com.wintermute.gmassistant.services.player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.gmassistant.helper.Tags;
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
        mp.start();
    }

    @Override
    public void onCreate()
    {
        player = PlayerOperations.getInstance();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Scene scene = intent.getParcelableExtra("scene");
        Track track = scene != null ? scene.getAmbience() : intent.getParcelableExtra("track");
        startForeground(3, createNotification(intent, "Ambience", CHANNEL_ID, AmbienceReceiver.class));

        if (track != null)
        {
            if (track.getDelay() == 1 && scene.getEffect() != null)
            {
                player.startAmbienceWithEffect(this, scene);
            } else
            {
                player.startByTag(this, track);
            }
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        PlayerOperations.getInstance().stopPlayer(Tags.AMBIENCE.value());
    }
}
