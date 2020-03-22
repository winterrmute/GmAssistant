package com.wintermute.gmassistant.view.boards.scenes;

import android.content.Intent;
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
import com.wintermute.gmassistant.operations.LightOperations;
import com.wintermute.gmassistant.view.StorageBrowser;
import com.wintermute.gmassistant.view.library.LibraryContent;
import com.wintermute.gmassistant.database.model.SceneDbModel;
import com.wintermute.gmassistant.database.model.Tags;
import com.wintermute.gmassistant.view.light.LightConfiguration;
import com.wintermute.gmassistant.view.model.Light;
import com.wintermute.gmassistant.view.model.Track;
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.operations.SceneOperations;
import com.wintermute.gmassistant.operations.TrackOperations;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles creating or editing new scenes and updating it on playlist content if related.
 *
 * @author wintermute
 */
public class SceneConfig extends AppCompatActivity
{

    public static final int LIGHT_FOR_SCENE = 4;
    private EditText nameField;

    private Button effect;
    private Button music;
    private Button ambience;

    private SeekBar effectVolume;
    private SeekBar musicVolume;
    private SeekBar ambienceVolume;

    private Switch delayMusic;
    private Switch delayAmbience;

    private Light configuredLight;
    private ImageView colorView;

    private String tag;
    private Long currentBoard;

    private SceneOperations operations;
    private PlayerOperations player;
    private LightOperations light;
    private Map<String, Object> content = new HashMap<>();
    private Map<String, Track> trackHolder = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_configuration);

        currentBoard = getIntent().getLongExtra("boardId", -1L);

        operations = new SceneOperations(getApplicationContext());
        player = new PlayerOperations();

        nameField = findViewById(R.id.scene_name);
        colorView = findViewById(R.id.selected_color);

        delayMusic = findViewById(R.id.delay_music);
        delayAmbience = findViewById(R.id.delay_ambience);

        previewPlayers();
        volumeBars();

        Button lightEffects = findViewById(R.id.set_light);
        lightEffects.setOnClickListener(v -> setLights());

        effect = findViewById(R.id.set_start_effect);
        effect.setOnClickListener(v -> selectTrack(Tags.EFFECT));

        music = findViewById(R.id.set_music);
        music.setOnClickListener(v -> selectTrack(Tags.MUSIC));

        ambience = findViewById(R.id.set_ambience);
        ambience.setOnClickListener(v -> selectTrack(Tags.AMBIENCE));

        Button sceneSubmit = findViewById(R.id.scene_submit);
        sceneSubmit.setOnClickListener(v -> submitScene());
    }

    private void selectTrack(Tags category)
    {
        browseFilesForTrack(category.ordinal());
        tag = category.name();
    }

    private void submitScene()
    {
        if (!nameField.getText().toString().equals(""))
        {
            content.put(SceneDbModel.NAME.value(), nameField.getText());
            content.put(SceneDbModel.BOARD_ID.value(), currentBoard);
            storeTracks();
            storeLight();
            operations.createScene(content);
            setResult(RESULT_OK);
            player.stopAll();
            goToSceneBoardView();
            finish();
        } else
        {
            Toast.makeText(this, "Please set scene name!", Toast.LENGTH_SHORT).show();
        }
    }

    private void goToSceneBoardView()
    {
        Intent sceneBoard = new Intent(getApplicationContext(), SceneBoard.class);
        sceneBoard.putExtra("boardId", currentBoard);
        startActivity(sceneBoard);
    }

    private void storeLight()
    {
        if (configuredLight != null)
        {
            configuredLight.setId(light.createLight(configuredLight));
        }
    }

    private void storeTracks()
    {
        TrackOperations operations = new TrackOperations(getApplicationContext());
        for (Track track : trackHolder.values())
        {
            if (null != track)
            {
                try
                {
                    track.setId(operations.storeTrackIfNotExist(track));
                    collectConfig(track);
                    content.put(track.getTag(), track);
                } catch (Exception e)
                {
                    Toast
                        .makeText(getApplicationContext(), "Could not add track: " + track.getName()
                            + " \nIf the track contains non characters like: \"?!,;\" etc., rename the file and "
                            + "try again", Toast.LENGTH_LONG)
                        .show();
                }
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

        playEffect.setOnClickListener(v -> playPreview(trackHolder.get(Tags.EFFECT.value()), playEffect));

        playMusic.setOnClickListener(v -> playPreview(trackHolder.get(Tags.MUSIC.value()), playMusic));

        playAmbience.setOnClickListener(v -> playPreview(trackHolder.get(Tags.AMBIENCE.value()), playAmbience));
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
                browseFiles = new Intent(SceneConfig.this, StorageBrowser.class);
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
        Intent lightConfigurator = new Intent(SceneConfig.this, LightConfiguration.class);
        startActivityForResult(lightConfigurator, LIGHT_FOR_SCENE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == LIGHT_FOR_SCENE)
            {
                configuredLight = data.getParcelableExtra("light");
                if (configuredLight != null)
                {
                    content.put("light", configuredLight);
                    light = new LightOperations(getApplicationContext());
                    colorView.setImageBitmap(light.extractColor(configuredLight));
                }
            } else
            {
                TrackOperations trackOperations = new TrackOperations(getApplicationContext());
                Track track = trackOperations.createTrack(data.getStringExtra("path"));
                String fileName = track.getName() == null ? new File(track.getPath()).getName() : track.getName();
                if (requestCode == Tags.EFFECT.ordinal())
                {
                    track.setTag(Tags.EFFECT.value());
                    effect.setText(fileName);
                } else if (requestCode == Tags.MUSIC.ordinal())
                {
                    track.setTag(Tags.MUSIC.value());
                    music.setText(fileName);
                } else if (requestCode == Tags.AMBIENCE.ordinal())
                {
                    track.setTag(Tags.AMBIENCE.value());
                    ambience.setText(fileName);
                }
                trackHolder.put(track.getTag(), track);
            }
        }
    }
}
