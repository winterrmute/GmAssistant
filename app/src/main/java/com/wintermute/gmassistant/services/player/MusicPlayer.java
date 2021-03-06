package com.wintermute.gmassistant.services.player;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.gmassistant.database.model.Tags;
import com.wintermute.gmassistant.view.model.Scene;
import com.wintermute.gmassistant.view.model.Track;
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.services.notifications.MusicReceiver;
import lombok.SneakyThrows;

/**
 * Handles the background music player.
 *
 * @author wintermute
 */
public class MusicPlayer extends BaseService
{

    public static final String CHANNEL_ID = "Music";
    private PlayerOperations player;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        player = PlayerOperations.getInstance();
    }

    @SneakyThrows
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Scene scene = intent.getParcelableExtra("scene");
        Track track = scene != null ? scene.getMusic() : intent.getParcelableExtra("track");
        startForeground(2, createNotification(intent, "Music", CHANNEL_ID, MusicReceiver.class));

        if (track != null)
        {
            if (scene != null && track.getDelay() == 1 && scene.getEffect() != null)
            {
                player.startMusicWithEffect(this, scene);
            } else
            {
                player.startByTag(this, track);
            }
            startForeground(2, createNotification(intent, "Music", CHANNEL_ID, MusicReceiver.class));
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        PlayerOperations.getInstance().stopPlayer(Tags.MUSIC.value());
    }
}
