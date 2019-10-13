package com.wintermute.soundboard.services.player;

import static android.media.MediaPlayer.create;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.wintermute.soundboard.database.dao.PlaylistContentDao;
import com.wintermute.soundboard.database.dao.SceneDao;
import com.wintermute.soundboard.database.dao.TrackDao;

public class JumpScareSound extends Service
    implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{

    private MediaPlayer mediaPlayer;
    private String playlistId;
    private String trackId;

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
        mediaPlayer.setOnCompletionListener(mp ->
        {
            Intent intent = new Intent(getBaseContext(), BackgroundMusic.class);
            String nextTrackId = getNextTrack(playlistId, trackId);
            intent.putExtra("trackId", nextTrackId);
            startService(intent);
        });
    }

    private String getNextTrack(String playlistId, String trackid){
        PlaylistContentDao pcd = new PlaylistContentDao(getBaseContext());
        String sceneId = pcd.getSceneId(playlistId, trackid);
        SceneDao dao = new SceneDao(getBaseContext());
        return dao.getNextTrack(sceneId);
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
