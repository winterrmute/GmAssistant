package com.wintermute.gmassistant.operations;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import com.wintermute.gmassistant.model.Player;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;

import java.util.HashMap;
import java.util.Map;

public class PlayerOperations
{
    private MediaPlayer effectPlayer;
    private MediaPlayer musicPlayer;
    private MediaPlayer ambiencePlayer;

    private Map<String, Player> playerByTag = new HashMap<>();

    private static PlayerOperations instance;

    public static PlayerOperations getInstance()
    {
        if (PlayerOperations.instance == null)
        {
            PlayerOperations.instance = new PlayerOperations();
        }
        return PlayerOperations.instance;
    }

    public void start(Context ctx, Track track)
    {
        if (track.getTag().equals("effect"))
        {
            effectPlayer = new MediaPlayer();
            effectPlayer = MediaPlayer.create(ctx, Uri.parse("file://" + track.getPath()));
            effectPlayer.start();
        } else if (track.getTag().equals("music"))
        {
            musicPlayer = new MediaPlayer();
            musicPlayer = MediaPlayer.create(ctx, Uri.parse("file://" + track.getPath()));
            musicPlayer.start();
        } else
        {
            ambiencePlayer = new MediaPlayer();
            ambiencePlayer = MediaPlayer.create(ctx, Uri.parse("file://" + track.getPath()));
            ambiencePlayer.start();
        }
    }

    public void stopPlayer(String tag)
    {
        if (tag.equals("effect"))
        {
            effectPlayer.stop();
            effectPlayer.release();
        } else if (tag.equals("music"))
        {
            musicPlayer.stop();
            musicPlayer.release();
        } else
        {
            ambiencePlayer.stop();
            ambiencePlayer.release();
        }
    }

    public void startWithEffect(Context ctx, Scene scene)
    {
        createMusic(ctx, scene.getAmbience());
        createAmbience(ctx, scene.getAmbience());

        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            public void run()
            {
                startMusic(ctx, scene.getMusic());
                startAmbience(ctx, scene.getAmbience());
            }
        }, scene.getEffect().getDuration());
    }

    public void startMusic(Context ctx, Track track)
    {
        createMusic(ctx, track);
        musicPlayer.start();
    }

    public void startEffect(Context ctx, Track track)
    {
        createEffect(ctx, track);
        effectPlayer.start();
    }

    public void startAmbience(Context ctx, Track track)
    {
        createAmbience(ctx, track);
        ambiencePlayer.start();
    }

    private void createEffect(Context ctx, Track track)
    {
        effectPlayer = new MediaPlayer();
        effectPlayer = MediaPlayer.create(ctx, Uri.parse("file://" + track.getPath()));
        //        effectPlayer.start();
    }

    private void createMusic(Context ctx, Track track)
    {
        musicPlayer = new MediaPlayer();
        musicPlayer = MediaPlayer.create(ctx, Uri.parse("file://" + track.getPath()));
        //        musicPlayer.start();
    }

    private void createAmbience(Context ctx, Track track)
    {
        ambiencePlayer = new MediaPlayer();
        ambiencePlayer = MediaPlayer.create(ctx, Uri.parse("file://" + track.getPath()));
        //        musicPlayer.start();
    }
}
