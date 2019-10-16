package com.wintermute.gmassistant.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.services.PlaylistCreateService;

/**
 * Represents activity in which the user can create new playlist.
 */
public class NewPlaylist extends AppCompatActivity
{

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(v ->
        {
            EditText playlistName = findViewById(R.id.playlist_name);
            if (!playlistName.getText().toString().equals(""))
            {
                PlaylistCreateService playlistCreator =
                    new PlaylistCreateService(this, playlistName.getText().toString(), path);
                playlistCreator.createPlaylistAndReferences();
                this.finish();
            } else
            {
                Toast.makeText(this, "playlist name must not be empty", Toast.LENGTH_SHORT).show();
            }
        });

        Button browseDevice = findViewById(R.id.browse_device);
        browseDevice.setOnClickListener(v ->
        {
            Intent fileBrowser = new Intent(NewPlaylist.this, FileBrowser.class);
            startActivityForResult(fileBrowser, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            path = data.getStringExtra("path");
        }
    }
}
