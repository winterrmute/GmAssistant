package com.wintermute.gmassistant.view.playlist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.TrackAdapter;
import com.wintermute.gmassistant.view.scenes.SceneConfig;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.dialogs.ListDialog;
import com.wintermute.gmassistant.handlers.PlayerHandler;
import com.wintermute.gmassistant.view.model.Track;

import java.util.ArrayList;
import java.util.Arrays;
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
    private String playlistId;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        trackDao = new TrackDao(this);
        renderFilesAsList();

        playlistId = PlaylistContentView.this.getIntent().getStringExtra("playlistId");

        songView.setOnItemClickListener((parent, view, position, id) ->
        {
            PlayerHandler handler = new PlayerHandler(PlaylistContentView.this);
//            handler.startPlayerByTrack(playlistId, allTracks.get(position).getId());
        });

        songView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            this.position = position;
            openDialog("set tag: \"music\"", "set tag: \"ambience\"", "set tag: \"effect\"", "manage scene", "delete");
            return true;
        });
    }

    private void openDialog(String... opts)
    {
        Intent dialog = new Intent(PlaylistContentView.this, ListDialog.class);
        dialog.putStringArrayListExtra("opts", new ArrayList<>(Arrays.asList(opts)));
        startActivityForResult(dialog, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        PlaylistContentDao dao = new PlaylistContentDao(PlaylistContentView.this);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1)
        {
            String selected = data.getStringExtra("selected");
            switch (selected)
            {
                case "set tag: \"music\"":
                    setTag(position, "music");
                    break;
                case "set tag: \"ambience\"":
                    setTag(position, "ambience");
                    break;
                case "set tag: \"effect\"":
                    setTag(position, "effect");
                    break;
                case "manage scene":
                    openDialog(prepareSceneConfigDialog());
                    break;
                case "edit":
//                    String sceneId = dao.getSceneIdForTrackInPlaylist(playlistId, allTracks.get(position).getId());
                    Intent sceneConfig = new Intent(PlaylistContentView.this, SceneConfig.class);
                    sceneConfig
                        .putExtra("edit", true)
//                        .putExtra("sceneId", sceneId)
                        .putExtra("trackId", allTracks.get(position).getId())
                        .putExtra("playlistId", playlistId);
                    startActivityForResult(sceneConfig, 1);
                    break;
                case "add new":
                    sceneConfig = new Intent(PlaylistContentView.this, SceneConfig.class);
                    sceneConfig
                        .putExtra("trackId", allTracks.get(position).getId())
                        .putExtra("playlistId", playlistId)
                        .putExtra("addSceneToTrack", true);
                    startActivityForResult(sceneConfig, 1);
                    break;
                case "DELETE":
                    dao = new PlaylistContentDao(PlaylistContentView.this);
//                    dao.deleteTrackFromPlaylist(playlistId, allTracks.get(position).getId());
                    renderFilesAsList();
                    break;
            }
        }
    }

    private String[] prepareSceneConfigDialog()
    {
        PlaylistContentDao dao = new PlaylistContentDao(this);
//        String sceneId = dao.getSceneIdForTrackInPlaylist(playlistId, allTracks.get(position).getId());
        String sceneId = "";
        if (null != sceneId)
        {
            return new String[] {"edit", "add new"};
        } else
        {
            return new String[] {"add new"};
        }
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
        renderFilesAsList();
    }

    /**
     * Renders songs contained by playlist.
     */
    void renderFilesAsList()
    {
        TrackAdapter songAdapter = new TrackAdapter(this, allTracks);
        songView = findViewById(R.id.track_list);
        songView.setAdapter(songAdapter);
    }
}
