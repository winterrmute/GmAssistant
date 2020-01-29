package com.wintermute.gmassistant.config;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.client.FileBrowser;
import com.wintermute.gmassistant.database.ObjectHandler;
import com.wintermute.gmassistant.database.dao.LightDao;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.model.Light;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;

import java.io.File;
import java.math.BigDecimal;

/**
 * Handles creating or editing new scenes and updating it on playlist content if related.
 *
 * @author wintermute
 */
public class SceneConfig extends AppCompatActivity
{

    private Light light;
    private Track startEffect;
    private Track music;
    private Track ambience;
    private String path;
    private EditText nameField;
    private boolean editScene;

    private TextView selectedEffect;
    private TextView selectedMusic;
    private TextView selectedAmbience;
    private ImageView selectedColor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_configuration);

        selectedEffect = findViewById(R.id.selected_effect);
        selectedMusic = findViewById(R.id.selected_music);
        selectedAmbience = findViewById(R.id.selected_ambience);
        selectedColor = findViewById(R.id.selected_color);

        boolean addSceneToTrack = getIntent().getBooleanExtra("addSceneToTrack", false);
        editScene = getIntent().getBooleanExtra("edit", false);
        nameField = findViewById(R.id.scene_name);

        SceneDao sceneDao = new SceneDao(this);
        TrackDao trackDao = new TrackDao(this);

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        Button setStartEffect = findViewById(R.id.set_start_effect);
        setStartEffect.setOnClickListener(v -> browseFilesForTrack(1));

        Button setMusic = findViewById(R.id.set_music);
        setMusic.setOnClickListener(v -> browseFilesForTrack(2));

        Button setAmbience = findViewById(R.id.set_ambience);
        setAmbience.setOnClickListener(v -> browseFilesForTrack(3));

        Button sceneSubmit = findViewById(R.id.scene_submit);
        sceneSubmit.setOnClickListener(v -> createOrUpdateScene(sceneDao));

        if (addSceneToTrack)
        {
            setStartEffect.setVisibility(View.GONE);
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
        if (!(startEffect == null && music == null))
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
            if (null != scene.getLight())
            {
                LightDao dao = new LightDao(this);
                selectedColor.setImageBitmap(extractColor(dao.getById(scene.getLight()).getColor()));
                dao.close();
            }
            if (null != scene.getStartEffect())
            {
                startEffect = trackDao.getById(scene.getStartEffect());
                selectedEffect.setText(startEffect.getName());
            }
            if (null != scene.getBackgroundMusic())
            {
                music = trackDao.getById(scene.getBackgroundMusic());
                selectedMusic.setText(music.getName());
            }
            if (null != scene.getBackgroundAmbience())
            {
                ambience = trackDao.getById(scene.getBackgroundAmbience());
                selectedAmbience.setText(ambience.getName());
            }
        } else
        {
            startEffect = trackDao.getById((getIntent().getStringExtra("trackId")));
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
     * Creates light effect for given backgroundMusic.
     */
    private void setLights()
    {
        Intent fileBrowser = new Intent(SceneConfig.this, LightConfig.class);
        startActivityForResult(fileBrowser, 4);
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

    /**
     * @param result scene to return.
     * @return scene filled with its attrs.
     */
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
        if (startEffect != null)
        {
            result.setStartEffect(startEffect.getId());
        }
        if (music != null)
        {
            result.setBackgroundMusic(music.getId());
        }
        if (ambience != null)
        {
            result.setBackgroundAmbience(ambience.getId());
        }
        return result;
    }

    /**
     * Inserts the scene into playlist content if empty or overwrites the existing one.
     */
    private void updatePlaylistContent(String sceneId)
    {
        String playlistId = getIntent().getStringExtra("playlistId");
        if (null != playlistId)
        {
            PlaylistContentDao dao = new PlaylistContentDao(this);
            dao.updateScene(sceneId, playlistId, startEffect.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            path = data.getStringExtra("path");
            TrackDao trackDao = new TrackDao(this);
            Track toUpdate;
            if (requestCode == 1)
            {
                startEffect = createTrackIfNotExist();
                toUpdate = trackDao.getById(startEffect.getId());
                toUpdate.setTag("effect");
                trackDao.update(toUpdate);
                selectedEffect.setText(startEffect.getName());
            } else if (requestCode == 2)
            {
                music = createTrackIfNotExist();
                toUpdate = trackDao.getById(music.getId());
                toUpdate.setTag("music");
                trackDao.update(toUpdate);
                selectedMusic.setText(music.getName());
            } else if (requestCode == 3)
            {
                ambience = createTrackIfNotExist();
                toUpdate = trackDao.getById(ambience.getId());
                toUpdate.setTag("ambience");
                trackDao.update(toUpdate);
                selectedAmbience.setText(ambience.getName());
            } else if (requestCode == 4)
            {
                Light dto = new Light();
                String color = String.valueOf(data.getIntExtra("color", 0));
                dto.setColor(color);
                dto.setBrightness(String.valueOf(data.getIntExtra("brightness", 0)));
                LightDao dao = new LightDao(this);
                dto.setId(String.valueOf(dao.insert(dto)));
                light = dao.getById(dto.getId());
                selectedColor.setImageBitmap(extractColor(color));
            }
        }
    }

    private Bitmap extractColor(String color)
    {
        Rect rect = new Rect(0, 0, 1, 1);
        Bitmap image = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setColor(new BigDecimal(color).intValue());
        canvas.drawRect(0, 0, 1, 1, paint);
        return image;
    }
}
