package com.wintermute.soundboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.client.ClientPlayer;
import com.wintermute.soundboard.client.FileBrowser;

public class Soundboard extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);
        grantUserPermission();

        Button playlist = findViewById(R.id.playlist);
        Button browseFiles = findViewById(R.id.browse_files);

        browseFiles.setOnClickListener(v ->
        {
            Intent fileBrowser = new Intent(Soundboard.this, FileBrowser.class);
            startActivity(fileBrowser);
        });

        playlist.setOnClickListener(v ->
        {
            Intent mediaPlayerClient = new Intent(Soundboard.this, ClientPlayer.class);
            startActivity(mediaPlayerClient);
        });
    }

    /**
     * Grants permissions for browsing directories the storage to access audio files.
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
}
