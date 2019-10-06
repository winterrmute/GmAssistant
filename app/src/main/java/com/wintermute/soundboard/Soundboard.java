package com.wintermute.soundboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.fragment.app.FragmentActivity;
import com.wintermute.soundboard.adapters.PlaylistAdapter;
import com.wintermute.soundboard.client.FileBrowser;
import com.wintermute.soundboard.dialogs.PlaylistSubmitter;

import java.util.ArrayList;
import java.util.List;

/**
 * User management panel.
 *
 * @author wintermute
 */
public class Soundboard extends FragmentActivity implements PlaylistSubmitter.OnInputListener
{
    private ListView playlists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);

        init();

        Button browseFiles = findViewById(R.id.browse_files);
        browseFiles.setOnClickListener(v ->
        {
            Intent fileBrowser = new Intent(Soundboard.this, FileBrowser.class);
            startActivity(fileBrowser);
        });

        Button playlist = findViewById(R.id.playlist);
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
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
            }
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        }

    }

    void renderPlaylists(List<String> userList)
    {
        PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, (ArrayList<String>) userList);
        playlists.setAdapter(playlistAdapter);
    }

    void init()
    {
        grantUserPermission();
        playlists = findViewById(R.id.playlists);
    }

    @Override
    public void sendInput(String playlistName)
    {
    }
}
