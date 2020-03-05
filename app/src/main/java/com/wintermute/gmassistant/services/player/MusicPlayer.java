package com.wintermute.gmassistant.services.player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.services.notifications.MusicReceiver;
import lombok.SneakyThrows;

/**
 * Handles the background music player.
 *
 * @author wintermute
 */
public class MusicPlayer extends BasePlayer
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{

    public static final String CHANNEL_ID = "Music";
    private MediaPlayer mediaPlayer = new MediaPlayer();
    private Scene scene;
    private PlayerOperations player;

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
        player = PlayerOperations.getInstance();
    }

    @SneakyThrows
    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        scene = intent.getParcelableExtra("scene");
        Track track = scene.getMusic();
        startForeground(2, createNotification(intent, "Music", CHANNEL_ID, MusicReceiver.class));
        player.startMusic(this, track);

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        mediaPlayer.stop();
    }
}
