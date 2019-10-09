package com.wintermute.soundboard.services.player;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class JumpScareSound extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener
{
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

    }
}
