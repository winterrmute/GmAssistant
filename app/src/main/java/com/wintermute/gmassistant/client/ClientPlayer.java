//package com.wintermute.soundboard.client;
//
//import android.content.Intent;
//import android.widget.MediaController;
//import androidx.appcompat.app.AppCompatActivity;
//import android.os.Bundle;
//import com.wintermute.soundboard.R;
//import com.wintermute.soundboard.services.player.MusicPlayerService;
//
///**
// * This client interacts with the MusicPlayerService. Allows to start, stop and pause music.
// *
// * @author wintermute
// */
//public class ClientPlayer extends AppCompatActivity implements MediaController.MediaPlayerControl
//{
//    @Override
//    protected void onCreate(Bundle savedInstanceState)
//    {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_media_player);
//
//        startPlayerService();
//    }
//
//    /**
//     * Starts the service and plays requested song.
//     */
//    void startPlayerService(){
//        start();
//    }
//
//    @Override
//    public void start()
//    {
//        Intent playerService = new Intent(ClientPlayer.this, MusicPlayerService.class);
//        playerService.putExtra("path", "/storage/emulated/0/Download/song.mp3");
//        startService(playerService);
//    }
//
//    @Override
//    public void pause()
//    {
//
//    }
//
//    @Override
//    public int getDuration()
//    {
//        return 0;
//    }
//
//    @Override
//    public int getCurrentPosition()
//    {
//        return 0;
//    }
//
//    @Override
//    public void seekTo(int pos)
//    {
//
//    }
//
//    @Override
//    public boolean isPlaying()
//    {
//        return false;
//    }
//
//    @Override
//    public int getBufferPercentage()
//    {
//        return 0;
//    }
//
//    @Override
//    public boolean canPause()
//    {
//        return false;
//    }
//
//    @Override
//    public boolean canSeekBackward()
//    {
//        return false;
//    }
//
//    @Override
//    public boolean canSeekForward()
//    {
//        return false;
//    }
//
//    @Override
//    public int getAudioSessionId()
//    {
//        return 0;
//    }
//}
