package com.wintermute.soundboard.dialogs;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.client.FileBrowser;
import com.wintermute.soundboard.model.Playlist;
import com.wintermute.soundboard.services.database.dao.PlaylistDao;

/**
 * Represents activity in which the user can create new playlist.
 */
public class CreatePlaylist extends AppCompatActivity
{

    EditText playlistName;
    PlaylistDao playlistDao;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        playlistDao = new PlaylistDao(this);
        playlistName = findViewById(R.id.playlist_name);

        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(v -> {
            createUserPlaylist();
            this.finish();
        });

        Button browseDevice = findViewById(R.id.browse_device);
        browseDevice.setOnClickListener(v -> {
            Intent fileBrowser = new Intent(CreatePlaylist.this, FileBrowser.class);
            startActivity(fileBrowser);
        });
    }

    /**
     * Creates new user playlist.
     */
    private void createUserPlaylist(){
        Playlist playlist = new Playlist();
        playlist.setName(playlistName.getText().toString());
        playlist.setId(playlistDao.insert(playlist));
    }
}
