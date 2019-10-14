package com.wintermute.soundboard.services.player;

import static android.media.MediaPlayer.create;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.soundboard.database.dao.LightDao;
import com.wintermute.soundboard.database.dao.SceneDao;
import com.wintermute.soundboard.database.dao.TrackDao;
import com.wintermute.soundboard.database.dto.Light;
import com.wintermute.soundboard.database.dto.Scene;
import com.wintermute.soundboard.handler.LightHandler;

public class JumpScareSound extends Service
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{

    private MediaPlayer mediaPlayer;
    private String playlistId;
    private String trackId;
    private String sceneId;

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

        sceneId = intent.getStringExtra("sceneId");
        trackId = intent.getStringExtra("trackId");
        playlistId = intent.getStringExtra("playlistId");
        startPlayback(getTrackPath(trackId));
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
        changeLight();
        mediaPlayer.setOnCompletionListener(mp ->
        {
            Intent intent = new Intent(getBaseContext(), BackgroundMusic.class);
            String nextTrackId = getNextTrack();
            intent.putExtra("trackId", nextTrackId);
            startService(intent);
        });
    }

    private void changeLight()
    {
        LightDao dao = new LightDao(getBaseContext());
        SceneDao sdao = new SceneDao(this);
        Scene scene = sdao.getById(sceneId);
        Light light = dao.getById(scene.getLight());
        LightHandler handler = new LightHandler(getBaseContext(), light);
        handler.req();
    }

    private String getNextTrack(){
        SceneDao dao = new SceneDao(getBaseContext());
        return dao.getById(sceneId).getNextTrack();
    }

    @Override
    public void onCreate()
    {
        mediaPlayer = new MediaPlayer();
    }

    private String getTrackPath(String trackId) {
        TrackDao dao = new TrackDao(getBaseContext());
        return dao.getTrackById(trackId).getPath();
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {

    }
}
