package com.wintermute.gmassistant.configurator;

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

/**
 * Creates new scene and updates dependency in related playlist content.
 *
 * @author wintermute
 */
public class SceneConfiguration extends AppCompatActivity
{

    private Track nextTrack;
    private Light light;
    private String sceneName;
    private String trackId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_configuration);

        EditText nameField = findViewById(R.id.scene_name);

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        Button nextTrack = findViewById(R.id.next_track);
        nextTrack.setOnClickListener(v -> setNextTrack());

        Button sceneSubmit = findViewById(R.id.scene_submit);
        sceneSubmit.setOnClickListener(v ->
        {
            String currentSceneName = nameField.getText().toString();
            if (currentSceneName.equals("")){
                Toast.makeText(this, "Scene name must not be empty!", Toast.LENGTH_SHORT).show();
            } else {

                this.sceneName = currentSceneName;
                updatePlaylistContent();
                finish();
            }
        });
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
     * Creates light effect for given nextTrack.
     */
    private void setLights()
    {
        Intent fileBrowser = new Intent(SceneConfiguration.this, LightConfiguration.class);
        startActivityForResult(fileBrowser, 2);
    }

    /**
     * Configures and creates new scene.
     */
    private Scene createScene()
    {
        Scene result = new Scene();
        result.setName(sceneName);
        if (light!= null) {
            result.setLight(light.getId());
        }
        else {
            result.setLight("-1");
        }
        if (nextTrack!= null)
        {
            result.setNextTrack(nextTrack.getId());
        }
        else {
            result.setNextTrack("-1");
        }
        if (trackId!= null)
        {
            result.setStartingTrack(trackId);
        }
        else {
            result.setNextTrack("-1");
        }
        SceneDao sceneDao = new SceneDao(this);
        result.setId(String.valueOf(sceneDao.insert(result)));
        return result;
    }

    private void updatePlaylistContent()
    {
        PlaylistContentDao pcd = new PlaylistContentDao(this);
        trackId = getIntent().getStringExtra("trackId");
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
            int brightness = data.getIntExtra("brightness", 0);
            dto.setColor(String.valueOf(color));
            dto.setBrightness(String.valueOf(brightness));
            dto.setId(String.valueOf(dao.insert(dto)));
            light = dao.getById(dto.getId());
        }
    }
}
