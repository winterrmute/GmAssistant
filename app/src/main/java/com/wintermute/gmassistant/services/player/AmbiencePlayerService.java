package com.wintermute.gmassistant.services.player;

import static android.media.MediaPlayer.create;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.gmassistant.services.notifications.AmbiencePlayerReceiver;

/**
 * Handles the ambient sound player.
 *
 * @author wintermute
 */
public class AmbiencePlayerService extends BasePlayerService
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener
{

    private static final String CHANNEL_ID = "Ambience";
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
        startForeground(1, createNotification(intent, "Ambience sound", CHANNEL_ID,  AmbiencePlayerReceiver.class));
        mediaPlayer = create(this, Uri.parse(getTrackPath(trackId)));
        mediaPlayer.setVolume(0.03f, 0.03f);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy(){
        mediaPlayer.stop();
    }
}
