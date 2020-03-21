package com.wintermute.gmassistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.hue.HueBridges;
import com.wintermute.gmassistant.view.boards.BoardsView;
import com.wintermute.gmassistant.view.library.LibraryContent;
import com.wintermute.gmassistant.view.playlist.PlaylistView;

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

        Button playlistView = findViewById(R.id.manage_playlists);
        playlistView.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, PlaylistView.class)));

        Button audioFileView = findViewById(R.id.manage_tracks);
        audioFileView.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, LibraryContent.class)));

        Button scenesBoard = findViewById(R.id.manage_scenes);
        scenesBoard.setOnClickListener(
            l -> startActivity(new Intent(GmAssistant.this, BoardsView.class).putExtra("boardCategory", "scenes")));

        Button hueManagementView = findViewById(R.id.manage_lights);
        hueManagementView.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, HueBridges.class)));

        Button effectBoards = findViewById(R.id.effect_board);
        effectBoards.setOnClickListener(
            l -> startActivity(new Intent(GmAssistant.this, BoardsView.class).putExtra("boardCategory", "effects")));
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
