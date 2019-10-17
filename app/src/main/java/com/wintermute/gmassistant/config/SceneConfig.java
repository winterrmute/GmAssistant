package com.wintermute.gmassistant.config;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.client.FileBrowser;
import com.wintermute.gmassistant.database.ObjectHandler;
import com.wintermute.gmassistant.database.dao.LightDao;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Light;
import com.wintermute.gmassistant.database.dto.Scene;
import com.wintermute.gmassistant.database.dto.Track;

import java.io.File;

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
    private String path;
    private boolean addSceneToTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_configuration);

        addSceneToTrack = getIntent().getBooleanExtra("addSceneToTrack", false);

        EditText nameField = findViewById(R.id.scene_name);

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        Button setStartTrack = findViewById(R.id.add_starting_track);
        setStartTrack.setOnClickListener(v ->
        {
            Intent fileBrowser = new Intent(SceneConfig.this, FileBrowser.class);
            fileBrowser.putExtra("selectTrack", true);
            startActivityForResult(fileBrowser, 1);
        });

        Button setNextTrack = findViewById(R.id.next_track);
        setNextTrack.setOnClickListener(v ->
        {
            Intent fileBrowser = new Intent(SceneConfig.this, FileBrowser.class);
            fileBrowser.putExtra("selectTrack", true);
            startActivityForResult(fileBrowser, 2);
        });

        Button sceneSubmit = findViewById(R.id.scene_submit);
        sceneSubmit.setOnClickListener(v ->
        {
            String currentSceneName = nameField.getText().toString();
            if (currentSceneName.equals(""))
            {
                Toast.makeText(this, "Scene name must not be empty!", Toast.LENGTH_SHORT).show();
            } else
            {
                if (startingTrack == null && nextTrack == null)
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

        if (addSceneToTrack) {
            setStartTrack.setVisibility(View.GONE);
            TrackDao dao = new TrackDao(this);
            startingTrack = dao.getById(getIntent().getStringExtra("trackId"));
        }

    }

    /**
     * @return new created track if the requested one did not exist in the database.
     */
    private Track createTrackIfNotExist()
    {
        if (path != null)
        {
            ObjectHandler objectHandler = new ObjectHandler(this);
            objectHandler.createTrack(new File(path));
            return objectHandler.createTrack(new File(path));
        } else
        {
            Toast.makeText(SceneConfig.this, "Next track not set!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    /**
     * Creates light effect for given nextTrack.
     */
    private void setLights()
    {
        Intent fileBrowser = new Intent(SceneConfig.this, LightConfig.class);
        startActivityForResult(fileBrowser, 3);
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
    //TODO: Refactor me
    private void updatePlaylistContent()
    {
        String playlistId = getIntent().getStringExtra("playlistId");
        PlaylistContentDao dao = new PlaylistContentDao(this);
        dao.insertOrUpdateScene(createScene().getId(), playlistId, startingTrack.getId());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        path = data.getStringExtra("path");
        if (requestCode == 1)
        {
            startingTrack = createTrackIfNotExist();
        } else if (requestCode == 2)
        {
            nextTrack = createTrackIfNotExist();
        } else if (requestCode == 3)
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
