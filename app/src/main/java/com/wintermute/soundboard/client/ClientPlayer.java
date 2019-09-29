package com.wintermute.soundboard.client;

import android.content.Intent;
import android.widget.MediaController;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.services.MediaPlayerService;

/**
 * This class interacts with the MediaPlayerService. Allows to start, stop and pause music.
 */
public class ClientPlayer extends AppCompatActivity implements MediaController.MediaPlayerControl
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        startPlayerService();
    }

    void startPlayerService(){
        Intent playerService = new Intent(ClientPlayer.this, MediaPlayerService.class);
        playerService.putExtra("path", "/storage/emulated/0/Download/song.mp3");
        startService(playerService);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public int getDuration()
    {
        return 0;
    }

    @Override
    public int getCurrentPosition()
    {
        return 0;
    }

    @Override
    public void seekTo(int pos)
    {

    }

    @Override
    public boolean isPlaying()
    {
        return false;
    }

    @Override
    public int getBufferPercentage()
    {
        return 0;
    }

    @Override
    public boolean canPause()
    {
        return false;
    }

    @Override
    public boolean canSeekBackward()
    {
        return false;
    }

    @Override
    public boolean canSeekForward()
    {
        return false;
    }

    @Override
    public int getAudioSessionId()
    {
        return 0;
    }
}
