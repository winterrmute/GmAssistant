package com.wintermute.gmassistant.handlers;

import android.content.Context;
import android.content.Intent;
import com.wintermute.gmassistant.database.model.Tags;
import com.wintermute.gmassistant.view.model.Scene;
import com.wintermute.gmassistant.view.model.Track;
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.services.player.AmbiencePlayer;
import com.wintermute.gmassistant.services.player.EffectPlayer;
import com.wintermute.gmassistant.services.player.MusicPlayer;

import java.util.Arrays;
import java.util.List;

/**
 * Manages triggering of player services.
 *
 * @author wintermute
 */
public class PlayerHandler
{

    private Context ctx;

    public PlayerHandler(Context ctx)
    {
        this.ctx = ctx;
    }

    /**
     * Plays single file from path by tagId.
     *
     * @param track on device to audio track.
     */
    public void play(Track track)
    {
        Class target;
        if (Tags.MUSIC.value().equals(track.getTag()))
        {
            target = MusicPlayer.class;
        } else if (Tags.AMBIENCE.value().equals(track.getTag()))
        {
            target = AmbiencePlayer.class;
        } else
        {
            target = EffectPlayer.class;
        }
        Intent player = new Intent(ctx, target);
        player.putExtra("track", track);
        ctx.startForegroundService(player);
    }

    /**
     * TODO: refactor
     *
     * @param scene
     */
    public void startPlayers(Scene scene)
    {
        clearScene();
        List<Track> tracks = Arrays.asList(scene.getEffect(), scene.getMusic(), scene.getAmbience());
        for (Track track : tracks)
        {
            if (track != null)
            {
                if (track.getTag().equals(Tags.EFFECT.value()))
                {
                    Intent player = new Intent(ctx, EffectPlayer.class);
                    player.putExtra("scene", scene);
                    ctx.startForegroundService(player);
                }
                if (track.getTag().equals(Tags.MUSIC.value()))
                {
                    Intent player = new Intent(ctx, MusicPlayer.class);
                    player.putExtra("scene", scene);
                    ctx.startForegroundService(player);
                }
                if (track.getTag().equals(Tags.AMBIENCE.value()))
                {
                    Intent player = new Intent(ctx, AmbiencePlayer.class);
                    player.putExtra("scene", scene);
                    ctx.startForegroundService(player);
                }
            }
        }
    }

    private void clearScene()
    {
        ctx.stopService(new Intent(ctx, EffectPlayer.class));
        ctx.stopService(new Intent(ctx, MusicPlayer.class));
        ctx.stopService(new Intent(ctx, AmbiencePlayer.class));
        PlayerOperations.getInstance().stopAll();
    }
}
