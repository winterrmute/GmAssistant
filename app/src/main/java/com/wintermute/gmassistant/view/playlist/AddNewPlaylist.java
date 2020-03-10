package com.wintermute.gmassistant.view.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.database.ObjectHandler;
import com.wintermute.gmassistant.view.StorageBrowser;
import com.wintermute.gmassistant.view.model.Playlist;

/**
 * Represents activity in which the user can create new playlist.
 */
public class AddNewPlaylist extends AppCompatActivity
{

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        Button browseDevice = findViewById(R.id.browse_device);
        browseDevice.setOnClickListener(v ->
        {
            Intent fileBrowser = new Intent(AddNewPlaylist.this, StorageBrowser.class);
            startActivityForResult(fileBrowser, 1);
        });

        Button submit = findViewById(R.id.create_playlist_submit);
        submit.setOnClickListener(v ->
        {
            EditText playlistName = findViewById(R.id.playlist_name);
            if (!playlistName.getText().toString().equals(""))
            {
                ObjectHandler objectHandler = new ObjectHandler(this);
                Playlist playlist = objectHandler.createPlaylist(playlistName.getText().toString());
                objectHandler.fillPlaylist(playlist, path);
                this.finish();
            } else
            {
                Toast.makeText(this, "playlist name must not be empty", Toast.LENGTH_SHORT).show();
            }
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
