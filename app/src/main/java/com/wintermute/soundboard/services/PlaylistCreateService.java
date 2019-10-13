package com.wintermute.soundboard.services;

import android.content.Context;
import com.wintermute.soundboard.database.dao.PlaylistContentDao;
import com.wintermute.soundboard.database.dao.PlaylistDao;
import com.wintermute.soundboard.database.dao.TrackDao;
import com.wintermute.soundboard.database.dto.PlaylistDto;
import com.wintermute.soundboard.database.dto.PlaylistContentDto;
import com.wintermute.soundboard.database.dto.TrackDto;

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
    private PlaylistDto createPlaylist()
    {
        PlaylistDao dao = new PlaylistDao(ctx);
        PlaylistDto result = new PlaylistDto();
        result.setName(playlistName);
        result.setId(String.valueOf(dao.insert(result)));
        dao.update(result);
        return result;
    }

    /**
     * Map all selected tracks to created playlist
     */
    private void fillPlaylistContent(PlaylistDto playlist, TrackDto track)
    {
        PlaylistContentDao dao = new PlaylistContentDao(ctx);
        PlaylistContentDto result = new PlaylistContentDto();
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
    private void addTracks(PlaylistDto playlist)
    {
        TrackDao dao = new TrackDao(ctx);
        FileBrowserService fs = new FileBrowserService();
        List<File> tracksFound = fs.collectTracks(browsePath);
        for (File file : tracksFound)
        {
            TrackDto track = new TrackDto();
            track.setName(file.getName());
            track.setPath(file.getPath());
            track.setId(dao.insert(track));

            fillPlaylistContent(playlist, track);
        }
    }
}
