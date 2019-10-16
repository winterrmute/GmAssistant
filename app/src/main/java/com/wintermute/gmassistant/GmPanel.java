package com.wintermute.gmassistant;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.adapters.PlaylistAdapter;
import com.wintermute.gmassistant.client.NewPlaylist;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.PlaylistDao;
import com.wintermute.gmassistant.database.dto.Playlist;
import com.wintermute.gmassistant.handler.PlayerHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * User management panel.
 *
 * @author wintermute
 */
public class GmPanel extends AppCompatActivity
{
    private ListView playlistView;
    private PlaylistDao playlistDao;
    private List<Playlist> playlists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_panel);

        init();

        Button addPlaylist = findViewById(R.id.playlist);
        addPlaylist.setOnClickListener(v ->
        {
            Intent createPlaylist = new Intent(GmPanel.this, NewPlaylist.class);
            startActivity(createPlaylist);
        });

        playlistView.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent playlistContent = new Intent(GmPanel.this, PlayerHandler.class);
            playlistContent.putExtra("playlistId", playlists.get(position).getId());
            startActivity(playlistContent);
        });

        playlistView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            AlertDialog.Builder dialog = new AlertDialog.Builder(GmPanel.this);
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
                        PlaylistContentDao pcd = new PlaylistContentDao(GmPanel.this);
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

    /**
     * Grants permissions to the application to access the storage.
     */
    void grantUserPermission(String permission)
    {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
        {
            if (shouldShowRequestPermissionRationale(permission))
            {
            }
            requestPermissions(new String[] {permission}, 0);
        }
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
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL,
                                Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.INTERNET};
        for (String permission : permissions)
        {
            grantUserPermission(permission);
        }
        playlistDao = new PlaylistDao(this);
        playlistView = findViewById(R.id.playlists);
        renderPlaylist();
    }
}