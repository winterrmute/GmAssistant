package com.wintermute.soundboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.adapters.PlaylistAdapter;
import com.wintermute.soundboard.client.NewPlaylist;
import com.wintermute.soundboard.client.PlaylistContentView;
import com.wintermute.soundboard.database.dao.PlaylistDao;
import com.wintermute.soundboard.model.Playlist;

import java.util.ArrayList;
import java.util.List;

/**
 * User management panel.
 *
 * @author wintermute
 */
public class Soundboard extends AppCompatActivity
{
    private ListView playlistView;
    private PlaylistDao playlistDao;
    private List<Playlist> listOfPlaylists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);

        init();

        Button playlist = findViewById(R.id.playlist);
        playlist.setOnClickListener(v ->
        {
            Intent createPlaylist = new Intent(Soundboard.this, NewPlaylist.class);
            startActivity(createPlaylist);
        });

        playlistView.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent playlistContent = new Intent(Soundboard.this, PlaylistContentView.class);
            playlistContent.putExtra("id", listOfPlaylists.get(position).getId());
            startActivity(playlistContent);
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
            return;
        }
    }

    private void renderPlaylist()
    {
        listOfPlaylists = playlistDao.getAll();
        listOfPlaylists =
            (listOfPlaylists != null || listOfPlaylists.size() != 0) ? listOfPlaylists : new ArrayList<>();
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, listOfPlaylists);
        playlistView.setAdapter(playlistAdapter);
    }

    private void init()
    {
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MEDIA_CONTENT_CONTROL,
                                Manifest.permission.MODIFY_AUDIO_SETTINGS};
        for (String permission : permissions)
        {
            grantUserPermission(permission);
        }
        playlistDao = new PlaylistDao(this);
        playlistView = findViewById(R.id.playlists);
        renderPlaylist();
    }
}
