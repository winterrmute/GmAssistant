package com.wintermute.gmassistant.operations;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import com.wintermute.gmassistant.database.model.Tags;
import com.wintermute.gmassistant.view.model.Scene;
import com.wintermute.gmassistant.view.model.Track;

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

    public void stopPlayer(String tag)
    {
        if (Tags.EFFECT.value().equals(tag))
        {
            try
            {
                effectPlayer.stop();
                effectPlayer.release();
            } catch (NullPointerException e)
            {

            }
            effectPlayer = new MediaPlayer();
        } else if (Tags.MUSIC.value().equals(tag))
        {
            try
            {
                musicPlayer.stop();
                musicPlayer.release();
            } catch (NullPointerException e)
            {

            }
            myHandler.removeCallbacks(delayedMusic);
            musicPlayer = new MediaPlayer();
        } else if (Tags.AMBIENCE.value().equals(tag))
        {
            try
            {
                ambiencePlayer.stop();
                ambiencePlayer.release();
            } catch (NullPointerException e)
            {

            }
            myHandler.removeCallbacks(delayedAmbience);
            ambiencePlayer = new MediaPlayer();
        }
    }

    public boolean isPlaying(String tag)
    {
        if (Tags.EFFECT.value().equals(tag) && effectPlayer != null)
        {
            return effectPlayer.isPlaying();
        } else if (Tags.MUSIC.value().equals(tag) && musicPlayer != null)
        {
            return musicPlayer.isPlaying();
        } else if (Tags.AMBIENCE.value().equals(tag) && ambiencePlayer != null)
        {
            return ambiencePlayer.isPlaying();
        }
        return false;
    }

    /**
     * Changes volume on player
     *
     * @param volume desired volume
     * @param tag target player
     */
    public void adjustVolume(long volume, String tag)
    {
        float vol = volume / 10.0f;
        if (Tags.EFFECT.value().equals(tag))
        {
            safeAdjustVolume(effectPlayer, vol);
        } else if (Tags.MUSIC.value().equals(tag))
        {
            safeAdjustVolume(musicPlayer, vol);
        } else if (Tags.AMBIENCE.value().equals(tag))
        {
            safeAdjustVolume(ambiencePlayer, vol);
        }
    }

    private void safeAdjustVolume(MediaPlayer player, float volume) {
        if (player == null) {
            player = new MediaPlayer();
        }
        player.setVolume(volume, volume);
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

    private void startMusic(Context ctx, Track track)
    {
        createMusic(ctx, track);
        musicPlayer.start();
    }

    public void startEffect(Context ctx, Track track)
    {
        createEffect(ctx, track);
        effectPlayer.start();
    }

    public void startByTag(Context ctx, Track track)
    {
        if (Tags.EFFECT.value().equals(track.getTag()))
        {
            startEffect(ctx, track);
        } else if (Tags.MUSIC.value().equals(track.getTag()))
        {
            startMusic(ctx, track);
        } else if (Tags.AMBIENCE.value().equals(track.getTag()))
        {
            startAmbience(ctx, track);
        }
    }

    private void startAmbience(Context ctx, Track track)
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
        if (track.getVolume() != null)
        {
                adjustVolume(track.getVolume(), track.getTag());
        }
        else {
            effectPlayer.setVolume(1f, 1f);
        }
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
        if (track.getVolume() != null)
        {
            adjustVolume(track.getVolume(), track.getTag());
        }
        else {
            musicPlayer.setVolume(0.3f, 0.3f);
        }
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
        if (track.getVolume() != null)
        {
            adjustVolume(track.getVolume(), track.getTag());
        } else {
            ambiencePlayer.setVolume(0.1f, 0.1f);
        }
    }
}
