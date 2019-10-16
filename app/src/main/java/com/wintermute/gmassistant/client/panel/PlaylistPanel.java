package com.wintermute.gmassistant.client.panel;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.PlaylistAdapter;
import com.wintermute.gmassistant.client.NewPlaylist;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.PlaylistDao;
import com.wintermute.gmassistant.database.dto.Playlist;
import com.wintermute.gmassistant.handler.PlaylistHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * User management panel.
 *
 * @author wintermute
 */
public class PlaylistPanel extends AppCompatActivity
{
    private ListView playlistView;
    private PlaylistDao playlistDao;
    private List<Playlist> playlists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_panel);

        init();

        Button addPlaylist = findViewById(R.id.playlist);
        addPlaylist.setOnClickListener(v ->
        {
            Intent createPlaylist = new Intent(PlaylistPanel.this, NewPlaylist.class);
            startActivity(createPlaylist);
        });

        playlistView.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent playlistContent = new Intent(PlaylistPanel.this, PlaylistHandler.class);
            playlistContent.putExtra("playlistId", playlists.get(position).getId());
            startActivity(playlistContent);
        });

        playlistView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(PlaylistPanel.this);
            dialog.setTitle(playlists.get(position).getName());
            String[] opts = {"RENAME", "DELETE"};
            dialog.setItems(opts, (opt, which) ->
            {
                opt.dismiss();
                switch (which)
                {
                    case 0:
                        Playlist playlist = playlists.get(position);
                        playlists.get(position).setName("implement me!");
                        playlistDao.update(playlist);
                        renderPlaylist();
                        break;
                    case 1:
                        playlistDao.delete(playlists.get(position));
                        PlaylistContentDao pcd = new PlaylistContentDao(PlaylistPanel.this);
                        pcd.deleteByPlaylistId(playlists.get(position).getId());
                        playlistDao.delete(playlists.get(position));
                        renderPlaylist();
                        break;
                }
            });
            dialog.show();
            return true;
        });
    }

    @Override
    public void onResume()
    {
        super.onResume();
        renderPlaylist();
    }

    private void renderPlaylist()
    {
        playlists = playlistDao.getAll();
        playlists = (playlists != null || playlists.size() != 0) ? playlists : new ArrayList<>();
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, playlists);
        playlistView.setAdapter(playlistAdapter);
    }

    private void init()
    {

        playlistDao = new PlaylistDao(this);
        playlistView = findViewById(R.id.playlists);
        renderPlaylist();
    }
}
