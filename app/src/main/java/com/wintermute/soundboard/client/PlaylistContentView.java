package com.wintermute.soundboard.client;

import android.content.Intent;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.adapters.AudioFileAdapter;
import com.wintermute.soundboard.model.Track;
import com.wintermute.soundboard.services.MediaPlayerService;
import com.wintermute.soundboard.services.database.dao.TrackDao;

import java.util.ArrayList;

/**
 * Playlists manager.
 *
 * @author wintermute
 */
public class PlaylistContentView extends AppCompatActivity
{

    private ListView songView;
    private ArrayList<Track> allTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);
        renderFilesAsList();

        songView.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent playerService = new Intent(PlaylistContentView.this, MediaPlayerService.class);
            playerService.putExtra("path", allTracks.get(position).getPath());
            startService(playerService);
        });
    }

    /**
     * Renders songs contained by playlist.
     */
    void renderFilesAsList()
    {
        songView = findViewById(R.id.audio_list);
        TrackDao trackDao = new TrackDao(this);

        allTracks = trackDao.getAll();

        AudioFileAdapter songAdapter = new AudioFileAdapter(this, allTracks);
        songView.setAdapter(songAdapter);
    }
}
