package com.wintermute.soundboard.manager;

import android.content.Intent;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.adapters.AudioFileAdapter;
import com.wintermute.soundboard.model.Track;
import com.wintermute.soundboard.services.player.BackgroundMusic;
import com.wintermute.soundboard.database.dao.TrackDao;

import java.util.List;

/**
 * Playlists manager.
 *
 * @author wintermute
 */
public class PlayerManager extends AppCompatActivity
{

    private ListView songView;
    private List<Track> allTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        renderFilesAsList();

        songView.setOnItemClickListener((parent, view, position, id) ->
        {
            Intent playerService = new Intent(PlayerManager.this, BackgroundMusic.class);
            playerService.putExtra("path", allTracks.get(position).getPath());
            startService(playerService);
        });
    }

    /**
     * Renders songs contained by playlist.
     */
    void renderFilesAsList()
    {
        TrackDao trackDao = new TrackDao(this);
        allTracks = trackDao.getReferencedTracks(this.getIntent().getStringExtra("id"));

        AudioFileAdapter songAdapter = new AudioFileAdapter(this, allTracks);
        songView = findViewById(R.id.audio_list);
        songView.setAdapter(songAdapter);
    }

    public void lel(){

    }
}
