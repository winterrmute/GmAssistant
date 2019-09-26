package com.wintermute.soundboard.services;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
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

    private MediaPlayer player;
    private ArrayList<Song> songs;
    private int songPos;

    private final IBinder musicBind = new MusicBinder();

    /**
     * Resets the player and plays a song from its uri.
     */
    public void playSong()
    {
        player.reset();
        Song playSong = songs.get(songPos);
        long currentSong = playSong.getId();
        Uri trackUri =
            ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, currentSong);

        try {
            player.setDataSource(getApplicationContext(), trackUri);
        } catch (Exception e) {
            Log.e("MediaPlayerService", "Error setting data source", e);
        }
        player.prepareAsync();
    }

    /**
     * sets song id picked from a list.
     */
    public void setSong(int songIndex){
        songPos = songIndex;
    }

    /**
     * Inits the media player after the service is created.
     */
    public void onCreate()
    {
        super.onCreate();
        songPos = 0;
        player = new MediaPlayer();
        initMusicPlayer();
    }

    /**
     * Loads the playlist for the MediaPlayerService.
     *
     * @param playList for the service to play.
     */
    public void setList(ArrayList<Song> playList)
    {
        songs = playList;
    }

    /**
     * sets up the media player.
     */
    public void initMusicPlayer()
    {
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);


        //player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //TODO: check for proper configuration of AudioAttributes
        player.setAudioAttributes(new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build());

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent)
    {
        player.stop();
        player.release();
        return false;
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

    /**
     * Interaction layer between services and activities.
     */
    public class MusicBinder extends Binder
    {
        public MediaPlayerService getService()
        {
            return MediaPlayerService.this;
        }
    }
}
