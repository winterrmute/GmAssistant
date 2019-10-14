package com.wintermute.soundboard.configurator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.client.FileBrowser;
import com.wintermute.soundboard.database.dao.LightDao;
import com.wintermute.soundboard.database.dao.PlaylistContentDao;
import com.wintermute.soundboard.database.dao.SceneDao;
import com.wintermute.soundboard.database.dao.TrackDao;
import com.wintermute.soundboard.database.dto.Light;
import com.wintermute.soundboard.database.dto.Scene;
import com.wintermute.soundboard.database.dto.Track;

import java.util.Objects;

/**
 * Creates new scene and updates dependency in related playlist content.
 *
 * @author wintermute
 */
public class SceneConfiguration extends AppCompatActivity
{

    private Track nextTrack;
    private Light light;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_manager);

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        Button nextTrack = findViewById(R.id.next_track);
        nextTrack.setOnClickListener(v -> setNextTrack());

        Button sceneSubmit = findViewById(R.id.scene_submit);
        sceneSubmit.setOnClickListener(v ->
        {
            updatePlaylistContent();
            finish();
        });
    }

    /**
     * Creates light effect for given nextTrack.
     */
    private void setLights()
    {
        Intent fileBrowser = new Intent(SceneConfiguration.this, LightConfiguration.class);
        startActivityForResult(fileBrowser, 2);
    }

    /**
     * Sets following nextTrack to current playing nextTrack.
     */
    private void setNextTrack()
    {
        Intent fileBrowser = new Intent(SceneConfiguration.this, FileBrowser.class);
        fileBrowser.putExtra("hasNextTrack", true);
        startActivityForResult(fileBrowser, 1);
    }

    /**
     * Configures and creates new scene.
     */
    private Scene createScene()
    {
        Scene result = new Scene();
        if (light != null)
        {
            result.setLight(light.getId());
        }
        if (nextTrack != null)
        {
            result.setNextTrack(Objects.requireNonNull(nextTrack.getId()));
        }
        SceneDao sceneDao = new SceneDao(this);
        result.setId(String.valueOf(sceneDao.insert(result)));
        return result;
    }

    private void updatePlaylistContent()
    {
        PlaylistContentDao pcd = new PlaylistContentDao(this);
        String trackId = getIntent().getStringExtra("trackId");
        String playlistId = getIntent().getStringExtra("playlistId");
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
        if (requestCode == 2)
        {
            LightDao dao = new LightDao(this);
            Light dto = new Light();
            int color = data.getIntExtra("color", 0);
            dto.setColor(String.valueOf(color));
            dto.setBrightness(String.valueOf(100));
            dto.setId(String.valueOf(dao.insert(dto)));
            light = dao.getById(dto.getId());
        }
    }
}
