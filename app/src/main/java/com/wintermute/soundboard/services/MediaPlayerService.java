package com.wintermute.soundboard.services;

import static android.media.MediaPlayer.create;

import android.app.Service;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.soundboard.model.Song;

import java.util.ArrayList;

/**
 * Handles the media player and client requests.
 *
 * @author wintermute
 */
public class MediaPlayerService extends Service
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
     * Prepares playlist.
     */
    private void startPlayback(String path)
    {
        mediaPlayer = create(this, Uri.parse(path));
        mediaPlayer.start();
    }

    Song currentSong(ArrayList<Song> playlist, int position)
    {
        return playlist.get(position);
    }

    @Override
    public void onCreate()
    {
        mediaPlayer = new MediaPlayer();
    }
}
