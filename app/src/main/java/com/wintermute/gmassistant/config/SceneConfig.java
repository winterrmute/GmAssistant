package com.wintermute.gmassistant.config;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
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
import com.wintermute.gmassistant.helper.Tags;
import com.wintermute.gmassistant.helper.SceneDbModel;
import com.wintermute.gmassistant.model.Light;
import com.wintermute.gmassistant.model.Track;
import com.wintermute.gmassistant.operations.SceneOperations;
import com.wintermute.gmassistant.operations.TrackOperations;
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

    private EditText nameField;

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

        nameField = findViewById(R.id.scene_name);

        fbs = new FileBrowserService();

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        Button setStartEffect = findViewById(R.id.set_start_effect);
        setStartEffect.setOnClickListener(v ->
        {
            browseFilesForTrack(Tags.EFFECT.ordinal());
            tag = Tags.EFFECT.name();
        });

        Button setMusic = findViewById(R.id.set_music);
        setMusic.setOnClickListener(v ->
        {
            browseFilesForTrack(Tags.MUSIC.ordinal());
            tag = Tags.MUSIC.name();
        });

        Button setAmbience = findViewById(R.id.set_ambience);
        setAmbience.setOnClickListener(v ->
        {
            browseFilesForTrack(Tags.AMBIENCE.ordinal());
            tag = Tags.AMBIENCE.name();
        });

        Button sceneSubmit = findViewById(R.id.scene_submit);
        sceneSubmit.setOnClickListener(v -> submitScene());
    }

    private void submitScene()
    {
        content.put(SceneDbModel.NAME.value(), nameField.getText());
        content.put(SceneDbModel.LIGHT.value(),
            content.get(SceneDbModel.LIGHT.value()) != null ? content.get(SceneDbModel.LIGHT.value()) : new Light());
        content.put(Tags.EFFECT.value(), content.get(Tags.EFFECT.value()));
        content.put(Tags.MUSIC.value(), content.get(Tags.AMBIENCE.value()));
        content.put(Tags.AMBIENCE.value(), content.get(Tags.AMBIENCE.value()));
        if (!nameField.getText().toString().equals(""))
        {
            operations.createScene(content);
            finish();
        } else
        {
            Toast.makeText(this, "Please set scene name!", Toast.LENGTH_SHORT).show();
        }
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
     * Creates light effect for given music.
     */
    private void setLights()
    {
        Intent lightConfigurator = new Intent(SceneConfig.this, LightConfig.class);
        startActivityForResult(lightConfigurator, 4);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            TrackOperations trackOperations = new TrackOperations(getApplicationContext());

            String path = data.getStringExtra("path");
            String fileName = path == null ? "" : new File(path).getName();
            Track track = trackOperations.getTrackOrCreateIfNotExist(path);
            if (requestCode == Tags.EFFECT.ordinal())
            {
                content.put(Tags.EFFECT.value(), track.getId());
                effectView.setText(fileName);
            } else if (requestCode == Tags.MUSIC.ordinal())
            {
                content.put(Tags.MUSIC.value(), track.getId());
                musicView.setText(fileName);
            } else if (requestCode == Tags.AMBIENCE.ordinal())
            {
                content.put(Tags.AMBIENCE.value(), track.getId());
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

                content.put(SceneDbModel.LIGHT.value(), dao.getById(light.getId()));

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
