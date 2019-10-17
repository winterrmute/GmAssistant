package com.wintermute.gmassistant.config;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.client.FileBrowser;
import com.wintermute.gmassistant.database.dao.LightDao;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Light;
import com.wintermute.gmassistant.database.dto.Scene;
import com.wintermute.gmassistant.database.dto.Track;
import com.wintermute.gmassistant.services.PlaylistCreateService;

/**
 * Creates new scene and updates dependency in related playlist content.
 *
 * @author wintermute
 */
public class SceneConfig extends AppCompatActivity
{

    private Light light;
    private String sceneName;
    private Track startingTrack;
    private Track nextTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_configuration);

        EditText nameField = findViewById(R.id.scene_name);

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        Button setStartTrack = findViewById(R.id.add_starting_track);
        setStartTrack.setOnClickListener(v -> selectTrack());

        Button setNextTrack = findViewById(R.id.next_track);
        setNextTrack.setOnClickListener(v -> selectTrack());

        Button sceneSubmit = findViewById(R.id.scene_submit);
        sceneSubmit.setOnClickListener(v ->
        {
            String currentSceneName = nameField.getText().toString();
            if (currentSceneName.equals(""))
            {
                Toast.makeText(this, "Scene name must not be empty!", Toast.LENGTH_SHORT).show();
            } else {
                if (startingTrack == null || nextTrack == null)
                {
                    Toast
                        .makeText(this, "The scene must contain either starting track or next track!",
                            Toast.LENGTH_SHORT)
                        .show();
                } else
                {
                    this.sceneName = currentSceneName;
                    updatePlaylistContent();
                    finish();
                }
            }
        });
    }

    /**
     * Sets following nextTrack to current playing nextTrack.
     */
    private void selectTrack()
    {
        Intent fileBrowser = new Intent(SceneConfig.this, FileBrowser.class);
        fileBrowser.putExtra("selectTrack", true);
        startActivityForResult(fileBrowser, 1);
    }

    /**
     * Creates light effect for given nextTrack.
     */
    private void setLights()
    {
        Intent fileBrowser = new Intent(SceneConfig.this, LightConfig.class);
        startActivityForResult(fileBrowser, 2);
    }

    /**
     * Configures and creates new scene.
     */
    private Scene createScene()
    {
        Scene result = new Scene();
        result.setName(sceneName);
        if (light != null)
        {
            result.setLight(light.getId());
        }
        if (nextTrack != null)
        {
            result.setNextTrack(nextTrack.getId());
        }
        if (startingTrack != null)
        {
            result.setStartingTrack(startingTrack.getId());
        }
        SceneDao sceneDao = new SceneDao(this);
        result.setId(String.valueOf(sceneDao.insert(result)));
        return result;
    }

    /**
     * Inserts the scene into playlist content if empty or overwrites the existing one.
     */
    private void updatePlaylistContent()
    {
        TrackDao trackDao = new TrackDao(this);
        startingTrack = trackDao.computeTrackIfAbsent(getIntent().getStringExtra("trackId"));

        String playlistId = getIntent().getStringExtra("playlistId");

        PlaylistContentDao dao = new PlaylistContentDao(this);
        dao.insertOrUpdateScene(createScene().getId(), playlistId, startingTrack.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            String path = data.getStringExtra("path");
            if (path != null)
            {
                TrackDao dao = new TrackDao(this);
                nextTrack = dao.getTrackByPath(path);
                if (nextTrack == null) {
                    //TODO: add new track if it does not exist.
                }
            } else
            {
                Toast.makeText(SceneConfig.this, "Next track not set!", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == 2)
        {
            Light dto = new Light();
            int color = data.getIntExtra("color", 0);
            int brightness = data.getIntExtra("brightness", 0);
            dto.setColor(String.valueOf(color));
            dto.setBrightness(String.valueOf(brightness));

            LightDao dao = new LightDao(this);
            dto.setId(String.valueOf(dao.insert(dto)));
            light = dao.getById(dto.getId());
        }
    }
}
