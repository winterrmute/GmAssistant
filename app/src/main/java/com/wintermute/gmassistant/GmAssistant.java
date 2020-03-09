package com.wintermute.gmassistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.client.view.LibraryContent;
import com.wintermute.gmassistant.client.view.PlaylistView;
import com.wintermute.gmassistant.client.view.effects.EffectBoards;
import com.wintermute.gmassistant.client.view.scenes.SceneView;
import com.wintermute.gmassistant.hue.HueBridgeRegistrator;
import com.wintermute.gmassistant.hue.HueBulbSelector;

/**
 * Startup activity. Provides Game masters panel.
 */
public class GmAssistant extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_assistant);

        grantUserPermission();

        Button playlistPanel = findViewById(R.id.manage_playlists);
        playlistPanel.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, PlaylistView.class)));

        Button trackPanel = findViewById(R.id.manage_tracks);
        trackPanel.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, LibraryContent.class)));

        Button scenePanel = findViewById(R.id.manage_scenes);
        scenePanel.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, SceneView.class)));

        Button lightPanel = findViewById(R.id.manage_lights);
        lightPanel.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, HueBridgeRegistrator.class)));

        Button effectBoards = findViewById(R.id.effect_board);
        effectBoards.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, EffectBoards.class)));
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
                shouldShowRequestPermissionRationale(permission);
                requestPermissions(new String[] {permission}, 0);
            }
        }
    }
}
