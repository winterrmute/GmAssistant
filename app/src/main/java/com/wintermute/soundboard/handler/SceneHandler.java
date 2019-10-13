package com.wintermute.soundboard.handler;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.client.FileBrowser;
import com.wintermute.soundboard.database.dao.PlaylistContentDao;
import com.wintermute.soundboard.database.dao.SceneDao;
import com.wintermute.soundboard.database.dao.TrackDao;
import com.wintermute.soundboard.database.dto.SceneDto;
import com.wintermute.soundboard.database.dto.TrackDto;

/**
 * Creates new scene and updates dependency in related playlist content.
 *
 * @author wintermute
 */
public class SceneHandler extends AppCompatActivity
{

    private String trackId;
    private String playlistId;
    private TrackDto nextTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_manager);

        trackId = getIntent().getStringExtra("trackId");
        playlistId = getIntent().getStringExtra("playlistId");

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        Button nextTrack = findViewById(R.id.next_track);
        nextTrack.setOnClickListener(v -> setNextTrack());

        Button sceneSubmit = findViewById(R.id.scene_submit);
        sceneSubmit.setOnClickListener(v -> {
            updatePlaylistContent();
            finish();
        });
    }

    /**
     * Creates light effect for given nextTrack.
     */
    private void setLights()
    {
        //TODO: Implement me.
    }

    /**
     * Sets following nextTrack to current playing nextTrack.
     */
    private void setNextTrack()
    {
        Intent fileBrowser = new Intent(SceneHandler.this, FileBrowser.class);
        fileBrowser.putExtra("hasNextTrack", true);
        startActivityForResult(fileBrowser, 1);
    }

    /**
     * Configures and creates new scene.
     */
    private SceneDto createScene()
    {
        SceneDto result = new SceneDto();
        //TODO: implement me.
        result.setLight("");
        result.setTrack(trackId);
        result.setNextTrack(nextTrack.getId());
        SceneDao sceneDao = new SceneDao(this);
        result.setId(String.valueOf(sceneDao.insert(result)));
        return result;
    }

    private void updatePlaylistContent()
    {
        PlaylistContentDao pcd = new PlaylistContentDao(this);
        pcd.addScene(createScene().getId(), playlistId, trackId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            TrackDao dao = new TrackDao(this);
            String path = data.getStringExtra("path");
            nextTrack = dao.getTrackByPath(path);
        }
    }
}
