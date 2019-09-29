package com.wintermute.soundboard.client;

import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.adapters.SongAdapter;
import com.wintermute.soundboard.services.FileBrowserService;

public class Playlist extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        renderFilesAsList();
    }

    void renderFilesAsList()
    {
        ListView songView = (ListView) findViewById(R.id.song_list);
//        SongAdapter songAdapter = new SongAdapter(this, new FileBrowserService().scanDir());
//        songView.setAdapter(songAdapter);
    }
}
