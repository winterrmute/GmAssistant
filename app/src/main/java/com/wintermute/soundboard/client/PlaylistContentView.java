package com.wintermute.soundboard.client;

import android.content.Intent;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.adapters.AudioFileAdapter;
import com.wintermute.soundboard.model.Playlist;
import com.wintermute.soundboard.model.PlaylistContent;
import com.wintermute.soundboard.model.Track;
import com.wintermute.soundboard.services.MediaPlayerService;
import com.wintermute.soundboard.database.dao.PlaylistContentDao;
import com.wintermute.soundboard.database.dao.PlaylistDao;
import com.wintermute.soundboard.database.dao.TrackDao;

import java.util.List;

/**
 * Playlists manager.
 *
 * @author wintermute
 */
public class PlaylistContentView extends AppCompatActivity
{

    private ListView songView;
    private List<Track> allTracks;
    private TrackDao trackDao;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        this.getIntent().getStringExtra("id");
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
        trackDao = new TrackDao(this);
        allTracks = trackDao.getReferencedTracks();

        AudioFileAdapter songAdapter = new AudioFileAdapter(this, allTracks);
        songView = findViewById(R.id.audio_list);
        songView.setAdapter(songAdapter);
    }
}
