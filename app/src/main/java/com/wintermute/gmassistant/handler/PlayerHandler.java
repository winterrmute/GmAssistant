package com.wintermute.gmassistant.handler;

import android.content.Context;
import android.content.Intent;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Scene;
import com.wintermute.gmassistant.services.player.AmbientSound;
import com.wintermute.gmassistant.services.player.BackgroundMusic;
import com.wintermute.gmassistant.services.player.JumpScareSound;

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
     * Start proper music player by tag.
     */
    public void startPlayerByScene(String sceneId)
    {

        SceneDao dao = new SceneDao(ctx);
        Scene scene = dao.getById(sceneId);

        String trackId;
        if (scene.getStartingTrack() == null)
        {
            trackId = scene.getNextTrack();
        } else
        {
            trackId = scene.getStartingTrack();
        }

        Intent player = prepareIntent(trackId, sceneId);
        ctx.startService(player);
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
     * Prepare intent to start player service.
     *
     * @param trackId of track which should be played.
     * @param sceneId of scene that should be triggered
     * @return
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
     */
    private Class specifyServicePlayer(String trackId)
    {
        TrackDao dao = new TrackDao(ctx);
        String tag = dao.getTrackById(trackId).getTag() == null ? tag = "music" : dao.getTrackById(trackId).getTag();

        if (tag.equals("ambiente"))
        {
            return AmbientSound.class;
        } else if (tag.equals("jumpscare"))
        {
            return JumpScareSound.class;
        } else
        {
            return BackgroundMusic.class;
        }
    }
}
