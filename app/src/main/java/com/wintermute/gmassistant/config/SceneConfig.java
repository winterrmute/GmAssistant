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
 * Handles creating or editing new scenes and updating it on playlist content if related.
 *
 * @author wintermute
 */
public class SceneConfig extends AppCompatActivity
{

    private Light light;
    private Track startingTrack;
    private Track nextTrack;
    private String path;
    private EditText nameField;
    private boolean editScene;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_configuration);

        boolean addSceneToTrack = getIntent().getBooleanExtra("addSceneToTrack", false);
        editScene = getIntent().getBooleanExtra("edit", false);
        nameField = findViewById(R.id.scene_name);

        SceneDao sceneDao = new SceneDao(this);
        TrackDao trackDao = new TrackDao(this);

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        Button setStartTrack = findViewById(R.id.add_starting_track);
        setStartTrack.setOnClickListener(v -> browseFilesForTrack(1));

        Button setNextTrack = findViewById(R.id.next_track);
        setNextTrack.setOnClickListener(v -> browseFilesForTrack(2));

        Button sceneSubmit = findViewById(R.id.scene_submit);
        sceneSubmit.setOnClickListener(v -> createOrUpdateScene(sceneDao));

        if (addSceneToTrack)
        {
            setStartTrack.setVisibility(View.GONE);
        }
        if (editScene || getIntent().getStringExtra("playlistId") != null)
        {
            getDetailsOnEdit(sceneDao, trackDao);
        }
    }

    /**
     * Start file browser for single track selection.
     *
     * @param requestCode to differ between starting and next track.
     */
    void browseFilesForTrack(int requestCode)
    {
        Intent fileBrowser = new Intent(SceneConfig.this, FileBrowser.class);
        fileBrowser.putExtra("selectTrack", true);
        startActivityForResult(fileBrowser, requestCode);
    }

    /**
     * Creates a scene if does not exists, updates the one that is edited.
     *
     * @param sceneDao scene data access object.
     */
    private void createOrUpdateScene(SceneDao sceneDao)
    {
        if (!(startingTrack == null && nextTrack == null))
        {
            if (nameField.getVisibility() == View.VISIBLE && "".equals(nameField.getText().toString()))
            {
                Toast.makeText(this, "Scene name must not be empty!", Toast.LENGTH_SHORT).show();
            } else
            {
                String sceneId;
                if (!editScene)
                {
                    sceneId = createScene(new Scene());
                } else
                {
                    sceneId = getIntent().getStringExtra("sceneId");
                    updateScene(sceneDao.getById(sceneId));
                }
                updatePlaylistContent(sceneId);
                finish();
            }
        } else
        {
            Toast
                .makeText(this, "The scene must contain either starting track or next track!", Toast.LENGTH_SHORT)
                .show();
        }
    }

    /**
     * Gets track information if editing existing scene.
     *
     * @param sceneDao data access object for scenes.
     * @param trackDao data access object for tracks.
     */
    public void getDetailsOnEdit(SceneDao sceneDao, TrackDao trackDao)
    {
        Scene scene = sceneDao.getById(getIntent().getStringExtra("sceneId"));
        if (null != scene)
        {
            nameField.setText(scene.getName());
            if (null != scene.getStartingTrack())
            {
                startingTrack = trackDao.getById(scene.getStartingTrack());
            }
            if (null != scene.getNextTrack())
            {
                nextTrack = trackDao.getById(scene.getNextTrack());
            }
        } else
        {
            startingTrack = trackDao.getById((getIntent().getStringExtra("trackId")));
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
    private String createScene(Scene scene)
    {
        SceneDao sceneDao = new SceneDao(this);
        Scene result = prepareScene(scene);
        return String.valueOf(sceneDao.insert(result));
    }

    private void updateScene(Scene scene)
    {
        SceneDao dao = new SceneDao(this);
        dao.updateScene(prepareScene(scene));
    }

    private Scene prepareScene(Scene result)
    {
        if (!"".equals(nameField.getText().toString()))
        {
            result.setName(nameField.getText().toString());
        }
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
        return result;
    }

    /**
     * Inserts the scene into playlist content if empty or overwrites the existing one.
     */
    //TODO: Refactor me
    private void updatePlaylistContent(String sceneId)
    {
        String playlistId = getIntent().getStringExtra("playlistId");
        if (null != playlistId)
        {
            PlaylistContentDao dao = new PlaylistContentDao(this);
            dao.updateScene(sceneId, playlistId, startingTrack.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
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
}
