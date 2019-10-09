package com.wintermute.soundboard.services.player;

import static android.media.MediaPlayer.create;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;

/**
 * Handles the media player and client requests.
 *
 * @author wintermute
 */
public class BackgroundMusic extends Service
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{

    MediaPlayer mediaPlayer = new MediaPlayer();

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {

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
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        startPlayback(intent.getStringExtra("path"));
        return Service.START_NOT_STICKY;
    }

    /**
     * Creates the media player containing an audio file to play.
     */
    private void startPlayback(String path)
    {
        mediaPlayer = create(this, Uri.parse(path));
        mediaPlayer.start();
    }

    @Override
    public void onCreate()
    {
        mediaPlayer.stop();
        mediaPlayer = new MediaPlayer();
    }
}
