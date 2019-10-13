package com.wintermute.soundboard.manager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.adapters.AudioFileAdapter;
import com.wintermute.soundboard.database.dao.PlaylistContentDao;
import com.wintermute.soundboard.database.dao.TrackDao;
import com.wintermute.soundboard.model.Track;
import com.wintermute.soundboard.services.player.AmbientSound;
import com.wintermute.soundboard.services.player.BackgroundMusic;
import com.wintermute.soundboard.services.player.JumpScareSound;

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
    private TrackDao trackDao;
    private String trackId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        trackDao = new TrackDao(this);
        renderFilesAsList();

        songView.setOnItemClickListener((parent, view, position, id) ->
        {
            trackId = allTracks.get(position).getId();
            String tag = allTracks.get(position).getTag();
            if (tag != null)
            {
                if (tag.equals("music"))
                {
                    startPlayer(BackgroundMusic.class);
                } else if (tag.equals("ambiente"))
                {
                    startPlayer(AmbientSound.class);
                } else if (tag.equals("jumpscare"))
                {
                    startPlayer(JumpScareSound.class);
                }
            } else
            {
                startPlayer(BackgroundMusic.class);
            }
        });

        songView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            AlertDialog.Builder b = new AlertDialog.Builder(PlayerManager.this);
            b.setTitle(allTracks.get(position).getName());
            String[] types = {"Set TAG: \"music\"", "Set TAG: \"ambiente\"", "Set TAG: \"jumpscare\"", "DELETE"};
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
                        PlaylistContentDao dao = new PlaylistContentDao(PlayerManager.this);
                        dao.deleteTrackFromPlaylist(PlayerManager.this.getIntent().getStringExtra("id"),
                            allTracks.get(position).getId());
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
    }

    /**
     * Renders songs contained by playlist.
     */
    void renderFilesAsList()
    {
        allTracks = trackDao.getReferencedTracks(this.getIntent().getStringExtra("id"));
        AudioFileAdapter songAdapter = new AudioFileAdapter(this, allTracks);
        songView = findViewById(R.id.audio_list);
        songView.setAdapter(songAdapter);
    }

    /**
     * Start proper music player by tag.
     *
     * @param type
     */
    private void startPlayer(Class type)
    {
        Intent player = new Intent(PlayerManager.this, type);
        player.putExtra("id", trackId);
        startService(player);
    }
}
