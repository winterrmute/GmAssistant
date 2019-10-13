package com.wintermute.soundboard.services.player;

import static android.media.MediaPlayer.create;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.soundboard.database.dao.TrackDao;

public class JumpScareSound extends Service
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{

    private MediaPlayer mediaPlayer;

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
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        startPlayback(getTrackPath(intent.getStringExtra("id")));
        return Service.START_NOT_STICKY;
    }

    /**
     * Creates the media player containing an audio file to play.
     */
    private void startPlayback(String path)
    {
        mediaPlayer.stop();
        mediaPlayer = create(this, Uri.parse(path));
        mediaPlayer.setVolume(1f, 1f);
        mediaPlayer.start();
        mediaPlayer.setOnCompletionListener(mp ->
        {
            Intent intent = new Intent(getBaseContext(), BackgroundMusic.class);
            intent.putExtra("id", "17");
            startService(intent);
        });
    }

    @Override
    public void onCreate()
    {
        mediaPlayer = new MediaPlayer();
    }

    private String getTrackPath(String trackId) {
        TrackDao dao = new TrackDao(getBaseContext());
        return dao.getTrack(trackId).getPath();
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {

    }
}
