package com.wintermute.soundboard.services;

import android.content.Context;
import com.wintermute.soundboard.database.dao.PlaylistContentDao;
import com.wintermute.soundboard.database.dao.PlaylistDao;
import com.wintermute.soundboard.database.dao.TrackDao;
import com.wintermute.soundboard.model.Playlist;
import com.wintermute.soundboard.model.PlaylistContent;
import com.wintermute.soundboard.model.Track;

import java.io.File;
import java.util.List;

/**
 * Inserts playlist, playlist_content and track database entries.
 *
 * @author wintermute
 */
public class PlaylistCreateService
{
    private Context ctx;
    private String playlistName;
    private String browsePath;
    private String soundType;

    /**
     * Creates an instance.
     *
     * @param ctx activity context.
     * @param playlistName for the new playlist.
     * @param path containing audio files.
     */
    public PlaylistCreateService(Context ctx, String playlistName, String path)
    {
        this.ctx = ctx;
        this.playlistName = playlistName;
        this.browsePath = path;
    }

    /**
     * Creates a playlist and references within database;
     */
    public void createPlaylistAndReferences()
    {
        addTracks(createPlaylist());
    }

    /**
     * Returns playlist object, the same as stored in database.
     *
     * @return new created playlist.
     */
    private Playlist createPlaylist()
    {
        PlaylistDao dao = new PlaylistDao(ctx);
        Playlist result = new Playlist();
        result.setName(playlistName);
        result.setId(dao.insert(result));
        dao.update(result);
        return result;
    }

    /**
     * Map all selected tracks to created playlist
     */
    private void fillPlaylistContent(Playlist playlist, Track track)
    {
        PlaylistContentDao dao = new PlaylistContentDao(ctx);
        PlaylistContent result = new PlaylistContent();
        result.setPlaylist(playlist.getId());
        result.setTrack(track.getId());
        dao.insert(result);
    }

    /**
     * Creates Tracks and the reference to created playlist. TODO: use database transaction for better performance TODO:
     * don´t create new songs
     *
     * @param playlist which should be referenced.
     */
    //TODO: Add with transaction
    private void addTracks(Playlist playlist)
    {
        TrackDao dao = new TrackDao(ctx);
        FileBrowserService fs = new FileBrowserService();
        List<File> tracksFound = fs.collectTracks(browsePath);
        for (File file : tracksFound)
        {
            Track.Builder builder =
                new Track.Builder().withName(file.getName()).withPath(file.getPath()).withType("");
            Track track = builder.withId(dao.insert(builder.build())).build();
            fillPlaylistContent(playlist, track);
        }
    }
}
