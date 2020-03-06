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
import android.widget.Switch;
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
import com.wintermute.gmassistant.operations.SceneTrackOperations;
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

    private Button effect;
    private Button music;
    private Button ambience;

    private SeekBar effectVolume;
    private SeekBar musicVolume;
    private SeekBar ambienceVolume;

    private Switch delayMusic;
    private Switch delayAmbience;

    private ImageView colorView;

    private String tag;
    private FileBrowserService fbs;

    private SceneOperations operations;
    private PlayerOperations player;
    private Map<String, Object> content = new HashMap<>();
    private Map<String, Track> trackHolder = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_configuration);

        operations = new SceneOperations(getApplicationContext());
        player = new PlayerOperations();

        nameField = findViewById(R.id.scene_name);
        colorView = findViewById(R.id.selected_color);

        delayMusic = findViewById(R.id.delay_music);
        delayAmbience = findViewById(R.id.delay_ambience);

        previewPlayers();
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
            setResult(RESULT_OK);
            finish();
        } else
        {
            Toast.makeText(this, "Please set scene name!", Toast.LENGTH_SHORT).show();
        }
    }

    private void storeTracks()
    {
        TrackOperations operations = new TrackOperations(getApplicationContext());
        SceneTrackOperations trackConfig = new SceneTrackOperations(getApplicationContext());
        for (Track track : trackHolder.values())
        {
            if (null != track)
            {
                track.setId(operations.storeTrackIfNotExist(track));
                collectConfig(track);
                Long trackWithConfig = trackConfig.storeTrackWithConfig(track);
                content.put(track.getTag(), trackWithConfig);
            }
        }
    }

    private void collectConfig(Track track)
    {
        if (Tags.EFFECT.value().equals(track.getTag()))
        {
            track.setVolume((long) effectVolume.getProgress());
            track.setDelay(0L);
        } else if (Tags.MUSIC.value().equals(track.getTag()))
        {
            track.setVolume((long) musicVolume.getProgress());
            int delay = delayMusic.isChecked() ? 1 : 0;
            track.setDelay((long) delay);
        } else if (Tags.AMBIENCE.value().equals(track.getTag()))
        {
            track.setVolume((long) ambienceVolume.getProgress());
            int delay = delayAmbience.isChecked() ? 1 : 0;
            track.setDelay((long) delay);
        }
    }

    private void previewPlayers()
    {
        ImageButton playEffect = findViewById(R.id.play_effect);
        ImageButton playMusic = findViewById(R.id.play_music);
        ImageButton playAmbience = findViewById(R.id.play_ambience);

        playEffect.setOnClickListener(v ->
        {
            playPreview(trackHolder.get(Tags.EFFECT.value()), playEffect);
        });

        playMusic.setOnClickListener(v ->
        {
            playPreview(trackHolder.get(Tags.MUSIC.value()), playMusic);
        });

        playAmbience.setOnClickListener(v ->
        {
            playPreview(trackHolder.get(Tags.AMBIENCE.value()), playAmbience);
        });
    }

    private void playPreview(Track track, ImageButton playerButton)
    {
        if (track != null)
        {
            String tag = track.getTag();
            if (tag != null)
            {
                if (player.isPlaying(tag))
                {
                    player.stopPlayer(tag);
                    playerButton.setImageResource(R.drawable.play);
                } else
                {
                    player.startByTag(this, track);
                    playerButton.setImageResource(R.drawable.end);
                }
            }
        }
    }

    private void volumeBars()
    {
        effectVolume = findViewById(R.id.effect_volume);
        musicVolume = findViewById(R.id.music_volume);
        ambienceVolume = findViewById(R.id.ambience_volume);
        Map<String, SeekBar> volumeForPlayer = new HashMap<>();
        volumeForPlayer.put(Tags.EFFECT.value(), effectVolume);
        volumeForPlayer.put(Tags.MUSIC.value(), musicVolume);
        volumeForPlayer.put(Tags.AMBIENCE.value(), ambienceVolume);
        setVolumeBarListener(volumeForPlayer);
    }

    private void setVolumeBarListener(Map<String, SeekBar> volumeWithTag)
    {
        for (Map.Entry<String, SeekBar> playerVol : volumeWithTag.entrySet())
        {
            playerVol.getValue().setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                {
                    if (trackHolder.get(playerVol.getKey()) != null)
                    {
                        player.adjustVolume(playerVol.getValue().getProgress(), playerVol.getKey());
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
                trackHolder.put(track.getTag(), track);
                effect.setText(fileName);
            } else if (requestCode == Tags.MUSIC.ordinal())
            {
                track.setTag(Tags.MUSIC.value());
                trackHolder.put(track.getTag(), track);
                music.setText(fileName);
            } else if (requestCode == Tags.AMBIENCE.ordinal())
            {
                track.setTag(Tags.AMBIENCE.value());
                trackHolder.put(track.getTag(), track);
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
