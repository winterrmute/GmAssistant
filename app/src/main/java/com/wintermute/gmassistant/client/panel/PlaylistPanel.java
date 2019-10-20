package com.wintermute.gmassistant.client.panel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.PlaylistAdapter;
import com.wintermute.gmassistant.client.AddNewPlaylist;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.PlaylistDao;
import com.wintermute.gmassistant.database.dto.Playlist;
import com.wintermute.gmassistant.dialogs.ListDialog;

import java.util.ArrayList;
import java.util.Arrays;
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
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_panel);

        init();

        Button addPlaylist = findViewById(R.id.playlist);
        addPlaylist.setOnClickListener(v ->
        {
            Intent createPlaylist = new Intent(PlaylistPanel.this, AddNewPlaylist.class);
            startActivity(createPlaylist);
        });

        playlistView.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent playlistContent = new Intent(PlaylistPanel.this, PlaylistContentPanel.class);
            playlistContent.putExtra("playlistId", playlists.get(position).getId());
            startActivity(playlistContent);
        });

        playlistView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            this.position = position;
            Intent dialog = new Intent(PlaylistPanel.this, ListDialog.class);
            dialog.putStringArrayListExtra("opts", new ArrayList<>(Arrays.asList("rename", "delete")));
            startActivityForResult(dialog, 1);
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1)
        {
            String selected = data.getStringExtra("selected");

            if ("rename".equals(selected))
            {
                Playlist playlist = playlists.get(position);
                playlists.get(position).setName("implement me!");
                playlistDao.update(playlist);
                renderPlaylist();
            } else if ("delete".equals(selected))
            {
                playlistDao.deleteById(playlists.get(position).getId());
                PlaylistContentDao pcd = new PlaylistContentDao(PlaylistPanel.this);
                pcd.deleteByPlaylistId(playlists.get(position).getId());
                playlistDao.deleteById(playlists.get(position).getId());
                renderPlaylist();
            }
        }
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
        playlists = (playlists != null) ? playlists : new ArrayList<>();
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
