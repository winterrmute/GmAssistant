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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.client.FileBrowser;
import com.wintermute.gmassistant.client.panel.LibraryContent;
import com.wintermute.gmassistant.database.dao.LightDao;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.helper.Categories;
import com.wintermute.gmassistant.model.Light;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.model.Track;
import com.wintermute.gmassistant.operator.SceneOperations;
import com.wintermute.gmassistant.services.FileBrowserService;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles creating or editing new scenes and updating it on playlist content if related.
 *
 * @author wintermute
 */
public class SceneConfig extends AppCompatActivity
{

    private Light light;
    private Track effect;
    private Track music;
    private Track ambience;
    private EditText nameField;
    private boolean editScene;

    private TextView effectView;
    private TextView musicView;
    private TextView ambienceView;
    private ImageView colorView;

    private String tag;
    private FileBrowserService fbs;

    private SceneOperations operations;
    private Map<String, Object> content = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_configuration);

        operations = new SceneOperations(getApplicationContext());

        effectView = findViewById(R.id.selected_effect);
        musicView = findViewById(R.id.selected_music);
        ambienceView = findViewById(R.id.selected_ambience);
        colorView = findViewById(R.id.selected_color);

        editScene = getIntent().getBooleanExtra("edit", false);
        nameField = findViewById(R.id.scene_name);

        fbs = new FileBrowserService();

        SceneDao sceneDao = new SceneDao(this);
        TrackDao trackDao = new TrackDao(this);

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        Button setStartEffect = findViewById(R.id.set_start_effect);
        setStartEffect.setOnClickListener(v ->
        {
            browseFilesForTrack(Categories.EFFECT.ordinal());
            tag = Categories.EFFECT.name();
        });

        Button setMusic = findViewById(R.id.set_music);
        setMusic.setOnClickListener(v ->
        {
            browseFilesForTrack(Categories.MUSIC.ordinal());
            tag = Categories.MUSIC.name();
        });

        Button setAmbience = findViewById(R.id.set_ambience);
        setAmbience.setOnClickListener(v ->
        {
            browseFilesForTrack(Categories.AMBIENCE.ordinal());
            tag = Categories.AMBIENCE.name();
        });

        Button sceneSubmit = findViewById(R.id.scene_submit);
//        sceneSubmit.setOnClickListener(v -> createOrUpdateScene(sceneDao));
        sceneSubmit.setOnClickListener(v -> submitScene());

        if (editScene || getIntent().getStringExtra("playlistId") != null)
        {
            getDetailsOnEdit(sceneDao, trackDao);
        }
    }

    private void submitScene(){
        content.put("name", nameField.getText());
        content.put("light", light != null ? light : new Light());
        content.put("effect", effect != null ? effect.getPath() : "");
        content.put("music", music != null ? music.getPath() : "");
        content.put("ambience", ambience != null ? ambience.getPath() : "");
        if (mandatoryFieldsFilled()){
            operations.createInstance(content);
            finish();
        } else {
            Toast.makeText(this, "Please set scene name!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean mandatoryFieldsFilled()
    {
        return !nameField.getText().toString().equals("");
    }

    /**
     * Start file browser for single track selection.
     *
     * @param requestCode to differ between starting and next track.
     */
    void browseFilesForTrack(int requestCode)
    {
        String[] choices = {"library", "internal storage"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select audio file from:");
        builder.setSingleChoiceItems(choices, -1, (dialog, item) ->
        {
            Intent browseFiles;
            dialog.dismiss();
            if (choices[item].equals("library"))
            {
                browseFiles = new Intent(SceneConfig.this, LibraryContent.class);
                browseFiles.putExtra("tag", tag);
            } else
            {
                browseFiles = new Intent(SceneConfig.this, FileBrowser.class);
            }
            browseFiles.putExtra("selectTrack", true);
            startActivityForResult(browseFiles, requestCode);
        });
        builder.setNegativeButton(R.string.cancel_result, (dialog, id) -> dialog.cancel());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Creates a scene if does not exists, updates the one that is edited.
     *
     * @param sceneDao scene data access object.
     */
    private void createOrUpdateScene(SceneDao sceneDao)
    {
        if (!(effect == null && music == null))
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
//                    updateScene(sceneDao.getById(sceneId));
                }
//                updatePlaylistContent(sceneId);
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
//        Scene scene = sceneDao.getById(getIntent().getStringExtra("sceneId"));
        Scene scene = new Scene();
        if (null != scene)
        {
            nameField.setText(scene.getName());
            if (null != scene.getLight())
            {
                LightDao dao = new LightDao(this);
                colorView.setImageBitmap(extractColor(dao.getById(scene.getLight().getId()).getColor()));
                dao.close();
            }
            //TODO: set names
            if (null != scene.getEffectPath())
            {
                effect = trackDao.getById(scene.getEffectPath());
                effectView.setText(effect.getName());
            }
            if (null != scene.getMusicPath())
            {
                music = trackDao.getById(scene.getMusicPath());
                musicView.setText(music.getName());
            }
            if (null != scene.getAmbiencePath())
            {
                ambience = trackDao.getById(scene.getAmbiencePath());
                ambienceView.setText(ambience.getName());
            }
        } else
        {
            effect = trackDao.getById((getIntent().getStringExtra("trackId")));
        }
    }

    /**
     * @return new created track if the requested one did not exist in the database.
     */
//    private Track createTrackIfNotExist()
//    {
//        if (path != null)
//        {
//            ObjectHandler objectHandler = new ObjectHandler(this);
//            objectHandler.createTrack(new File(path));
//            return objectHandler.createTrack(new File(path));
//        } else
//        {
//            Toast.makeText(SceneConfig.this, "Next track not set!", Toast.LENGTH_SHORT).show();
//        }
//        return null;
//    }

    /**
     * Creates light effect for given music.
     */
    private void setLights()
    {
        Intent lightConfigurator = new Intent(SceneConfig.this, LightConfig.class);
        startActivityForResult(lightConfigurator, 4);
    }

    /**
     * Configures and creates new scene.
     */
    private String createScene(Scene scene)
    {

        SceneDao sceneDao = new SceneDao(this);
        Scene result = prepareScene(scene);
        return null;
//        return String.valueOf(sceneDao.insert(result));
    }

//    private void updateScene(Scene scene)
//    {
//        SceneDao dao = new SceneDao(this);
//        dao.updateScene(prepareScene(scene));
//    }

    /**
     * @param result scene to return.
     * @return scene filled with its attrs.
     */
    private Scene prepareScene(Scene result)
    {
        if (!nameField.getText().toString().equals(""))
        {
            result.setName(nameField.getText().toString());
        }
        if (null != light)
        {
            result.setLight(light);
        }
        if (null != effect)
        {
            result.setEffectPath(effect.getPath());
        }
        if (null != music)
        {
            result.setMusicPath(music.getPath());
        }
        if (null != ambience)
        {
            result.setAmbiencePath(ambience.getPath());
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
            dao.updateScene(sceneId, playlistId, effect.getId());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            String path = data.getStringExtra("path");
            String fileName = path == null ? "" : new File(path).getName();

            TrackDao trackDao = new TrackDao(this);
            Track toUpdate;
            if (requestCode == Categories.EFFECT.ordinal())
            {
                content.put(Categories.EFFECT.value(), path);
                effectView.setText(fileName);
            } else if (requestCode == Categories.MUSIC.ordinal())
            {
                content.put(Categories.MUSIC.value(), path);
                musicView.setText(fileName);
            } else if (requestCode == Categories.AMBIENCE.ordinal())
            {
                content.put(Categories.AMBIENCE.value(), path);
                ambienceView.setText(fileName);
            } else if (requestCode == 4)
            {
                //TODO: extract to light operations
                Light light = new Light();
                String color = String.valueOf(data.getIntExtra("color", 0));
                light.setColor(color);
                light.setBrightness(String.valueOf(data.getIntExtra("brightness", 0)));

                LightDao dao = new LightDao(this);
                light.setId(dao.insert(light));

                content.put("light", dao.getById(light.getId()));

                colorView.setImageBitmap(extractColor(color));
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
