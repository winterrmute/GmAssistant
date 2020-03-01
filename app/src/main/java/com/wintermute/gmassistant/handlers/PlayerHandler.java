package com.wintermute.gmassistant.handlers;

import android.content.Context;
import android.content.Intent;
import com.wintermute.gmassistant.helper.Tags;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;
import com.wintermute.gmassistant.operations.TrackOperations;
import com.wintermute.gmassistant.services.player.AmbiencePlayerService;
import com.wintermute.gmassistant.services.player.AudioPlayer;
import com.wintermute.gmassistant.services.player.EffectPlayerService;
import com.wintermute.gmassistant.services.player.MusicPlayerService;

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
            target = MusicPlayerService.class;
        } else if (tag == Tags.AMBIENCE.ordinal())
        {
            target = AmbiencePlayerService.class;
        } else
        {
            target = EffectPlayerService.class;
        }
        Intent player = new Intent(ctx, target);
        player.putExtra("track", path);
        ctx.startService(player);
    }

    /**
     * Start proper music player by tag.
     */
    public void startPlayerByScene(Scene scene)
    {
        Track[] tracks = {scene.getEffect(), scene.getMusic(), scene.getAmbience()};
        for (Track track : tracks)
        {
            if (track != null)
            {
                new Thread()
                {
                    @Override
                    public void run()
                    {
                        Intent player = new Intent(ctx, AudioPlayer.class);
                        player.putExtra("path", track.getPath());
                        ctx.startService(player);
                    }
                }.start();
            }
        }
    }


    private Track getTrackForScene(Track track)
    {
        TrackOperations operations = new TrackOperations(ctx);
        return operations.get(String.valueOf(track.getId()));
    }
}
