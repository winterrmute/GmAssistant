package com.wintermute.soundboard.client;

import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.R;

/**
 * Playlists manager.
 *
 * @author wintermute
 */
public class PlaylistView extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        renderFilesAsList();
    }

    /**
     * Renders songs contained by playlist.
     */
    void renderFilesAsList()
    {
        ListView songView = findViewById(R.id.audio_list);
//        AudioFileAdapter songAdapter = new AudioFileAdapter(this, new FileBrowserService().scanDir());
//        songView.setAdapter(songAdapter);
    }
}
