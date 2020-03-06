package com.wintermute.gmassistant.config;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.client.FileBrowser;
import com.wintermute.gmassistant.client.panel.LibraryContent;
import com.wintermute.gmassistant.database.dao.LightDao;
import com.wintermute.gmassistant.helper.SceneDbModel;
import com.wintermute.gmassistant.helper.Tags;
import com.wintermute.gmassistant.model.Light;
import com.wintermute.gmassistant.model.Track;
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.operations.SceneOperations;
import com.wintermute.gmassistant.operations.TrackOperations;
import com.wintermute.gmassistant.services.FileBrowserService;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handles creating or editing new scenes and updating it on playlist content if related.
 *
 * @author wintermute
 */
public class SceneConfig extends AppCompatActivity
{

    private EditText nameField;

    private Button effect;
    private Button music;
    private Button ambience;

    private ImageView colorView;

    private String tag;
    private FileBrowserService fbs;

    private SceneOperations operations;
    private PlayerOperations player;
    private Map<String, Object> content = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_configuration);

        operations = new SceneOperations(getApplicationContext());
        player = new PlayerOperations();

        nameField = findViewById(R.id.scene_name);
        colorView = findViewById(R.id.selected_color);

        playPreview();
        volumeBars();

        fbs = new FileBrowserService();

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        effect = findViewById(R.id.set_start_effect);
        effect.setOnClickListener(v ->
        {
            browseFilesForTrack(Tags.EFFECT.ordinal());
            tag = Tags.EFFECT.name();
        });

        music = findViewById(R.id.set_music);
        music.setOnClickListener(v ->
        {
            browseFilesForTrack(Tags.MUSIC.ordinal());
            tag = Tags.MUSIC.name();
        });

        ambience = findViewById(R.id.set_ambience);
        ambience.setOnClickListener(v ->
        {
            browseFilesForTrack(Tags.AMBIENCE.ordinal());
            tag = Tags.AMBIENCE.name();
        });

        Button sceneSubmit = findViewById(R.id.scene_submit);
        sceneSubmit.setOnClickListener(v -> submitScene());
    }

    private void playPreview()
    {
        ImageButton playEffect = findViewById(R.id.play_effect);
        ImageButton playMusic = findViewById(R.id.play_music);
        ImageButton playAmbience = findViewById(R.id.play_ambience);

        playEffect.setOnClickListener(v ->
        {
            if (content.get(Tags.EFFECT.value()) != null)
            {
                if (player.isPlaying(Tags.EFFECT.value()))
                {
                    player.stopPlayer(Tags.EFFECT.value());
                    playEffect.setImageResource(R.drawable.play);
                } else
                {
                    player.startEffect(this, (Track) content.get(Tags.EFFECT.value()));
                    playEffect.setImageResource(R.drawable.end);
                }
            }
        });

        playMusic.setOnClickListener(v ->
        {
            if (content.get(Tags.MUSIC.value()) != null)
            {
                if (player.isPlaying(Tags.MUSIC.value()))
                {
                    player.stopPlayer(Tags.MUSIC.value());
                    playMusic.setImageResource(R.drawable.play);
                } else
                {
                    player.startMusic(this, (Track) content.get(Tags.MUSIC.value()));
                    playMusic.setImageResource(R.drawable.end);
                }
            }
        });

        playAmbience.setOnClickListener(v ->
        {
            if (content.get(Tags.AMBIENCE.value()) != null)
            {
                if (player.isPlaying(Tags.AMBIENCE.value()))
                {
                    player.stopPlayer(Tags.AMBIENCE.value());
                    playAmbience.setImageResource(R.drawable.play);
                } else
                {
                    player.startEffect(this, (Track) content.get(Tags.AMBIENCE.value()));
                    playMusic.setImageResource(R.drawable.end);
                }
            }
        });
    }

    private void volumeBars()
    {
        SeekBar effect = findViewById(R.id.effect_volume);
        effect.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (content.get(Tags.EFFECT.value()) != null)
                {
                    player.adjustVolume(effect.getProgress(), Tags.EFFECT.value());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        SeekBar music = findViewById(R.id.music_volume);
        music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (content.get(Tags.MUSIC.value()) != null)
                {
                    player.adjustVolume(music.getProgress(), Tags.MUSIC.value());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
        SeekBar ambience = findViewById(R.id.ambience_volume);
        ambience.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (content.get(Tags.AMBIENCE.value()) != null)
                {
                    player.adjustVolume(ambience.getProgress(), Tags.AMBIENCE.value());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    private void submitScene()
    {
        if (!nameField.getText().toString().equals(""))
        {
            content.put(SceneDbModel.NAME.value(), nameField.getText());
            content.put(SceneDbModel.LIGHT.value(),
                content.get(SceneDbModel.LIGHT.value()) != null ? content.get(SceneDbModel.LIGHT.value())
                                                                : new Light());
            storeTracks();
            operations.createScene(content);
            finish();
        } else
        {
            Toast.makeText(this, "Please set scene name!", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeTracks()
    {
        List<Track> tracks =
            Arrays.asList((Track) content.get(Tags.EFFECT.value()), (Track) content.get(Tags.MUSIC.value()),
                (Track) content.get(Tags.AMBIENCE.value()));

        TrackOperations operations = new TrackOperations(getApplicationContext());
        for (Track track : tracks)
        {
            if (null != track)
            {
                operations.storeTrackIfNotExist(track);
            }
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

            Track track = trackOperations.createTrack(data.getStringExtra("path"));
            String fileName = track.getName() == null ? new File(track.getPath()).getName() : track.getName();
            if (requestCode == Tags.EFFECT.ordinal())
            {
                track.setTag(Tags.EFFECT.value());
                content.put(Tags.EFFECT.value(), track);
                effect.setText(fileName);
            } else if (requestCode == Tags.MUSIC.ordinal())
            {
                track.setTag(Tags.MUSIC.value());
                content.put(Tags.MUSIC.value(), track);
                music.setText(fileName);
            } else if (requestCode == Tags.AMBIENCE.ordinal())
            {
                track.setTag(Tags.AMBIENCE.value());
                content.put(Tags.AMBIENCE.value(), track);
                ambience.setText(fileName);
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
