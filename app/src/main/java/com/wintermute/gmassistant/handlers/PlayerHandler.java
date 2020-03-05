package com.wintermute.gmassistant.handlers;

import android.content.Context;
import android.content.Intent;
import com.wintermute.gmassistant.helper.Tags;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;
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
     * @param path on device to audio track.
     * @param tag to specify player service.
     */
    public void play(String path, int tag)
    {
        Class target;
        if (tag == Tags.MUSIC.ordinal())
        {
            target = MusicPlayer.class;
        } else if (tag == Tags.AMBIENCE.ordinal())
        {
            target = AmbiencePlayer.class;
        } else
        {
            target = EffectPlayer.class;
        }
        Intent player = new Intent(ctx, target);
        player.putExtra("track", path);
        ctx.startService(player);
    }

    /**
     * TODO: refactor
     *
     * @param scene
     */
    public void startPlayers(Scene scene)
    {
        List<Track> tracks = Arrays.asList(scene.getEffect(), scene.getMusic(), scene.getAmbience());
        for (Track track : tracks)
        {
            if (track.getTag().equals("effect"))
            {
                Intent player = new Intent(ctx, EffectPlayer.class);
                player.putExtra("scene", scene);
                ctx.startForegroundService(player);
            }
            if (track.getTag().equals("music"))
            {
                Intent player = new Intent(ctx, MusicPlayer.class);
                player.putExtra("scene", scene);
                ctx.startForegroundService(player);
            }
            if (track.getTag().equals("ambience"))
            {
                Intent player = new Intent(ctx, AmbiencePlayer.class);
                player.putExtra("scene", scene);
                ctx.startForegroundService(player);
            }
        }
    }
}
