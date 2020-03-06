package com.wintermute.gmassistant.operations;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import com.wintermute.gmassistant.helper.Tags;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;

public class PlayerOperations
{
    private MediaPlayer effectPlayer;
    private MediaPlayer musicPlayer;
    private MediaPlayer ambiencePlayer;

    private Handler myHandler = new Handler();
    private Runnable delayedMusic;
    private Runnable delayedAmbience;

    private static PlayerOperations instance;

    public static PlayerOperations getInstance()
    {
        if (PlayerOperations.instance == null)
        {
            PlayerOperations.instance = new PlayerOperations();
        }
        return PlayerOperations.instance;
    }

    public void stopAll()
    {
        if (effectPlayer != null)
        {
            effectPlayer.stop();
        }
        if (musicPlayer != null)
        {
            musicPlayer.stop();
        }
        if (ambiencePlayer != null)
        {
            ambiencePlayer.stop();
        }

        myHandler.removeCallbacks(delayedMusic);
        myHandler.removeCallbacks(delayedAmbience);
    }

    public boolean stopPlayer(String tag)
    {
        if (tag.equals("effect") && effectPlayer != null)
        {
            effectPlayer.stop();
            effectPlayer.release();
            effectPlayer = new MediaPlayer();
            return true;
        } else if (tag.equals("music") && musicPlayer != null)
        {
            musicPlayer.stop();
            musicPlayer.release();
            musicPlayer = new MediaPlayer();
            return true;
        } else if (tag.equals("ambience") && ambiencePlayer != null)
        {
            ambiencePlayer.stop();
            ambiencePlayer.release();
            ambiencePlayer = new MediaPlayer();
            return true;
        }
        return false;
    }

    public boolean isPlaying(String tag){
        if (Tags.EFFECT.value().equals(tag) && effectPlayer != null)
        {
            return effectPlayer.isPlaying();
        } else if (Tags.MUSIC.value().equals(tag)  && musicPlayer != null)
        {
            return musicPlayer.isPlaying();
        } else if (Tags.AMBIENCE.value().equals(tag)  && ambiencePlayer != null)
        {
            return ambiencePlayer.isPlaying();
        }
        return false;
    }

    public void adjustVolume(int volume, String tag)
    {
        float vol = volume / 10.0f;
        if (Tags.EFFECT.value().equals(tag))
        {
            effectPlayer.setVolume(vol, vol);
        } else if (Tags.MUSIC.value().equals(tag))
        {
            musicPlayer.setVolume(vol, vol);
        } else if (Tags.AMBIENCE.value().equals(tag))
        {
            ambiencePlayer.setVolume(vol, vol);
        }
    }

    public void startMusicWithEffect(Context ctx, Scene scene)
    {
        delayedMusic = () ->
        {
            startMusic(ctx, scene.getMusic());
        };
        myHandler.postDelayed(delayedMusic, scene.getEffect().getDuration());
    }

    public void startAmbienceWithEffect(Context ctx, Scene scene)
    {
        delayedAmbience = () ->
        {
            startAmbience(ctx, scene.getAmbience());
        };
        myHandler.postDelayed(delayedAmbience, scene.getEffect().getDuration());
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
        if (effectPlayer != null)
        {
            effectPlayer.stop();
        }
        effectPlayer = new MediaPlayer();
        effectPlayer = MediaPlayer.create(ctx, Uri.parse("file://" + track.getPath()));
    }

    private void createMusic(Context ctx, Track track)
    {
        if (musicPlayer != null)
        {
            musicPlayer.stop();
        }
        musicPlayer = new MediaPlayer();
        musicPlayer = MediaPlayer.create(ctx, Uri.parse("file://" + track.getPath()));
        musicPlayer.setLooping(true);
    }

    private void createAmbience(Context ctx, Track track)
    {
        if (ambiencePlayer != null)
        {
            ambiencePlayer.stop();
        }
        ambiencePlayer = new MediaPlayer();
        ambiencePlayer = MediaPlayer.create(ctx, Uri.parse("file://" + track.getPath()));
        ambiencePlayer.setLooping(true);
    }
}
