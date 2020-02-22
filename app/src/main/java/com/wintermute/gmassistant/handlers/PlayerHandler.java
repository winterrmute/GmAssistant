package com.wintermute.gmassistant.handlers;

import android.content.Context;
import android.content.Intent;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.helper.Categories;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;
import com.wintermute.gmassistant.services.player.AmbiencePlayerService;
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
        if (tag == Categories.MUSIC.ordinal())
        {
            target = MusicPlayerService.class;
        } else if (tag == Categories.AMBIENCE.ordinal())
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
    public void startPlayerByScene(String sceneId)
    {
        SceneDao dao = new SceneDao(ctx);
        Scene scene = dao.getById(sceneId);
        String trackId = null;
        if (null != scene.getEffect() || null != scene.getMusic())
        {
            if (scene.getEffect() == null)
            {
                play(scene.getMusic().getPath(), Categories.valueOf(scene.getMusic().getTag()).ordinal());
            } else
            {
                play(scene.getMusic().getPath(), Categories.valueOf(scene.getMusic().getTag()).ordinal());
            }
            Intent player = prepareIntent(trackId, sceneId);
            ctx.startService(player);
        }
    }

    /**
     * Start proper music player by tag.
     */
    public void startPlayerByTrack(String playlistId, String trackId)
    {
        PlaylistContentDao dao = new PlaylistContentDao(ctx);
        String sceneId = dao.getSceneId(playlistId, trackId);
        Intent player = prepareIntent(trackId, sceneId);
        ctx.startService(player);
    }

    /**
     * Starts player service.
     *
     * @param track to start playing
     */
    public void startPlaying(Track track)
    {
        Intent player = new Intent(ctx, specifyServicePlayer(track.getId()));
        player.putExtra("trackId", track.getId());

        if (track.getTag().equals("ambience"))
        {
            player = new Intent(ctx, AmbiencePlayerService.class);
        } else if (track.getTag().equals("effect"))
        {
            player = new Intent(ctx, EffectPlayerService.class);
        } else
        {
            player = new Intent(ctx, EffectPlayerService.class);
        }
        ctx.startService(player);
    }

    /**
     * Prepare intent to start player service.
     *
     * @param trackId of track which should be played.
     * @param sceneId of scene that should be triggered
     * @return intent prepared to start
     * @deprecated will will be removed in future
     */
    private Intent prepareIntent(String trackId, String sceneId)
    {
        Intent player = new Intent(ctx, specifyServicePlayer(trackId));
        player.putExtra("trackId", trackId);
        player.putExtra("sceneId", sceneId);
        return player;
    }

    /**
     * @param trackId to identify the right player.
     * @return player that should be triggered.
     * @deprecated will be removed in future
     */
    private Class specifyServicePlayer(String trackId)
    {
        TrackDao dao = new TrackDao(ctx);
        String tag =
            dao.computeTrackIfAbsent(trackId).getTag() == null ? Categories.MUSIC.name() : dao.computeTrackIfAbsent(trackId).getTag();

        if (tag.equals(Categories.AMBIENCE.name()))
        {
            return AmbiencePlayerService.class;
        } else if (tag.equals(Categories.EFFECT.name()))
        {
            return EffectPlayerService.class;
        } else
        {
            return MusicPlayerService.class;
        }
    }
}
