package com.wintermute.soundboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.adapters.PlaylistAdapter;
import com.wintermute.soundboard.client.PlaylistContentView;
import com.wintermute.soundboard.dialogs.CreatePlaylist;
import com.wintermute.soundboard.services.database.dao.PlaylistDao;

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
    private List<String> listOfPlaylists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);

        init();

        Button playlist = findViewById(R.id.playlist);
        playlist.setOnClickListener(v ->
        {
            Intent createPlaylist = new Intent(Soundboard.this, CreatePlaylist.class);
            startActivity(createPlaylist);
        });

        playlistView.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent playlistIntent = new Intent(Soundboard.this, PlaylistContentView.class);
            startActivity(playlistIntent);
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
    void grantUserPermission()
    {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
            {
            }
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return;
        }
    }

    private void renderPlaylist(){
        listOfPlaylists = playlistDao.getPlaylistNames();
        listOfPlaylists = (listOfPlaylists != null || listOfPlaylists.size() != 0) ? listOfPlaylists : new ArrayList<>() ;
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, (ArrayList<String>) listOfPlaylists);
        playlistView.setAdapter(playlistAdapter);
    }

    private void init()
    {
        grantUserPermission();
        playlistDao = new PlaylistDao(this);
        playlistView = findViewById(R.id.playlists);
        renderPlaylist();
    }
}
