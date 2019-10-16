package com.wintermute.gmassistant.handler;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.TrackAdapter;
import com.wintermute.gmassistant.configurator.SceneConfiguration;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Track;

import java.util.List;

/**
 * Playlists manager.
 *
 * @author wintermute
 */
public class PlaylistHandler extends AppCompatActivity
{

    private ListView songView;
    private List<Track> allTracks;
    private TrackDao trackDao;
    private String playlistId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        trackDao = new TrackDao(this);
        renderFilesAsList();

        playlistId = PlaylistHandler.this.getIntent().getStringExtra("playlistId");

        songView.setOnItemClickListener((parent, view, position, id) ->
        {
            PlayerHandler handler = new PlayerHandler(PlaylistHandler.this);
            handler.startPlayerByTrack(playlistId, allTracks.get(position).getId());
        });

        songView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            AlertDialog.Builder b = new AlertDialog.Builder(PlaylistHandler.this);
            b.setTitle(allTracks.get(position).getName());
            String[] types =
                {"Set TAG: \"music\"", "Set TAG: \"ambiente\"", "Set TAG: \"jumpscare\"", "Add scene", "DELETE"};
            b.setItems(types, (dialog, which) ->
            {
                dialog.dismiss();
                switch (which)
                {

                    case 0:
                        setTag(position, "music");
                        break;
                    case 1:
                        setTag(position, "ambiente");
                        break;
                    case 2:
                        setTag(position, "jumpscare");
                        break;
                    case 3:
                        Intent sceneManager = new Intent(PlaylistHandler.this, SceneConfiguration.class);
                        sceneManager.putExtra("trackId", allTracks.get(position).getId());
                        sceneManager.putExtra("playlistId", playlistId);
                        startActivity(sceneManager);
                        break;
                    case 4:
                        PlaylistContentDao dao = new PlaylistContentDao(PlaylistHandler.this);
                        playlistId = PlaylistHandler.this.getIntent().getStringExtra("playlistId");
                        dao.deleteTrackFromPlaylist(playlistId, allTracks.get(position).getId());
                        renderFilesAsList();
                        break;
                }
            });
            b.show();
            return true;
        });
    }

    /**
     * Updates tag for selected trackId.
     *
     * @param position position in list.
     * @param tag to be set.
     */
    private void setTag(int position, String tag)
    {
        allTracks.get(position).setTag(tag);
        trackDao.update(allTracks.get(position));
        renderFilesAsList();
    }

    /**
     * Renders songs contained by playlist.
     */
    void renderFilesAsList()
    {
        allTracks = trackDao.getReferencedTracks(this.getIntent().getStringExtra("playlistId"));
        TrackAdapter songAdapter = new TrackAdapter(this, allTracks);
        songView = findViewById(R.id.track_list);
        songView.setAdapter(songAdapter);
    }
}
