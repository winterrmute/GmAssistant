package com.wintermute.soundboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ListView;
import androidx.fragment.app.FragmentActivity;
import com.wintermute.soundboard.adapters.PlaylistAdapter;
import com.wintermute.soundboard.client.FileBrowser;
import com.wintermute.soundboard.dialogs.PlaylistSubmitter;
import java.util.ArrayList;

/**
 * User management panel.
 *
 * @author wintermute
 */
public class Soundboard extends FragmentActivity implements PlaylistSubmitter.OnInputListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);
        grantUserPermission();

        getUserPlaylists();

        Button playlist = findViewById(R.id.playlist);
        Button browseFiles = findViewById(R.id.browse_files);

        browseFiles.setOnClickListener(v ->
        {
            Intent fileBrowser = new Intent(Soundboard.this, FileBrowser.class);
            startActivity(fileBrowser);
        });

        playlist.setOnClickListener(v ->
        {
            PlaylistSubmitter playlistSubmitter = new PlaylistSubmitter();
            playlistSubmitter.show(getSupportFragmentManager(), "playlist_submitter");
        });
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

    void getUserPlaylists()
    {
        //TODO: implement generating playlists

        ListView playlists = findViewById(R.id.playlists);
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, new ArrayList<>());
        playlists.setAdapter(playlistAdapter);
    }

    @Override
    public void sendInput(String playlistName)
    {
        //implementMe
    }
}
