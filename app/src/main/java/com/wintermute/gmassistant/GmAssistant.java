package com.wintermute.gmassistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.client.panel.LightPanel;
import com.wintermute.gmassistant.client.panel.PlaylistPanel;
import com.wintermute.gmassistant.client.panel.ScenePanel;

public class GmAssistant extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_assistant);

        grantUserPermission();

        Button playlistPanel = findViewById(R.id.manage_playlists);
        playlistPanel.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, PlaylistPanel.class)));

        Button scenePanel = findViewById(R.id.manage_scenes);
        scenePanel.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, ScenePanel.class)));

        Button lightPanel = findViewById(R.id.manage_lights);
        lightPanel.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, LightPanel.class)));
    }

    /**
     * Grants permissions to the application to access the storage.
     */
    void grantUserPermission()
    {
        String[] permissions =
            {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.MEDIA_CONTENT_CONTROL,
             android.Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.INTERNET};
        for (String permission : permissions)
        {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
            {
                if (shouldShowRequestPermissionRationale(permission))
                {
                }
                requestPermissions(new String[] {permission}, 0);
            }
        }
    }
}
