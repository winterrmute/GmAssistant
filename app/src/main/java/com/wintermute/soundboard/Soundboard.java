package com.wintermute.soundboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.client.MediaPlayer;

public class Soundboard extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);

        Button playlist = findViewById(R.id.playlist);

        playlist.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v)
            {
                Intent mediaPlayerClient = new Intent(Soundboard.this, MediaPlayer.class);
                startActivity(mediaPlayerClient);
            }
        });

        grantUserPermission();
    }

    /**
     * Grants permissions for browsing directories the storage to access audio files.
     */
    void grantUserPermission(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return;
        }
    }
}
