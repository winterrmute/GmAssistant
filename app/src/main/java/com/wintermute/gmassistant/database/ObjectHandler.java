package com.wintermute.gmassistant.database;

import android.content.Context;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.PlaylistDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.model.Playlist;
import com.wintermute.gmassistant.model.PlaylistContent;
import com.wintermute.gmassistant.model.Track;
import com.wintermute.gmassistant.services.FileBrowserService;

import java.io.File;

public class ObjectHandler
{
    private Context ctx;

    public ObjectHandler(Context ctx)
    {
        this.ctx = ctx;
    }

    public Track createTrack(File file)
    {
        TrackDao dao = new TrackDao(ctx);
        Track track = new Track();
        track.setName(file.getName());
        track.setPath(file.getPath());
        track.setId(dao.insert(track));
        return track;
    }

    public Playlist createPlaylist(String playlistName)
    {
        PlaylistDao dao = new PlaylistDao(ctx);
        Playlist result = new Playlist();
        result.setName(playlistName);
        result.setId(String.valueOf(dao.insert(result)));
        return result;
    }

    private void createPlaylistContent(Playlist playlist, Track track)
    {
        PlaylistContentDao dao = new PlaylistContentDao(ctx);
        PlaylistContent result = new PlaylistContent();
        result.setPlaylist(playlist.getId());
        result.setTrack(track.getId());
        dao.insert(result);
    }

    public void fillPlaylist(Playlist playlist, String path)
    {
        FileBrowserService fs = new FileBrowserService();
        //        List<File> tracksFound = fs.collectTracks(path);
        //        for (File track : tracksFound)
        //        {
        //            createPlaylistContent(playlist, createTrack(track));
        //        }
    }
}
