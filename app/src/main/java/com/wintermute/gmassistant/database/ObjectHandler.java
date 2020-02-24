package com.wintermute.gmassistant.database;

import android.content.Context;
import com.wintermute.gmassistant.database.dao.PlaylistDao;
import com.wintermute.gmassistant.model.Playlist;
import com.wintermute.gmassistant.services.FileBrowserService;

public class ObjectHandler
{
    private Context ctx;

    public ObjectHandler(Context ctx)
    {
        this.ctx = ctx;
    }

    public Playlist createPlaylist(String playlistName)
    {
        PlaylistDao dao = new PlaylistDao(ctx);
        Playlist result = new Playlist();
        result.setName(playlistName);
        result.setId(String.valueOf(dao.insert(result)));
        return result;
    }

    public void fillPlaylist(Playlist playlist, String path)
    {
        FileBrowserService fs = new FileBrowserService();
        //        List<File> tracksFound = fs.collectTracks(path);
        //        for (File track : tracksFound)
        //        {
        //            createPlaylistContent(playlist, getTrackOrCreateIfNotExist(track));
        //        }
    }
}
