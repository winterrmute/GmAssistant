package com.wintermute.gmassistant.view.boards;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.EffectsAdapter;
import com.wintermute.gmassistant.database.dao.EffectsDao;
import com.wintermute.gmassistant.database.model.Tags;
import com.wintermute.gmassistant.operations.BoardOperations;
import com.wintermute.gmassistant.operations.LightOperations;
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.operations.TrackOperations;
import com.wintermute.gmassistant.services.LightConnection;
import com.wintermute.gmassistant.view.StorageBrowser;
import com.wintermute.gmassistant.view.light.LightConfiguration;
import com.wintermute.gmassistant.view.model.Light;
import com.wintermute.gmassistant.view.model.Track;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Board containing effect sounds.
 */
public class EffectBoard extends AppCompatActivity
{

    private static final int COLLECT_TRACKS = 1;
    private static final int LIGHT_CONFIGURATION = 2;
    private List<Track> effects;
    private GridView effectsGrid;
    private SeekBar volume;
    private PlayerOperations player;
    private ImageButton stopPlayer;
    private Button lightEffects;
    private Long currentBoard;
    private Light light;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect_board);
        init();
        displayBoard();
        if (getIntent().getBooleanExtra("newEffectBoard", false))
        {
            startActivityForResult(new Intent(this, StorageBrowser.class), COLLECT_TRACKS);
        }
    }

    private void init()
    {
        currentBoard = getIntent().getLongExtra("boardId", -1L);
        effects = new ArrayList<>();
        volume = findViewById(R.id.volume);
        effectsGrid = findViewById(R.id.effect_grid);
        stopPlayer = findViewById(R.id.stop_player);
        lightEffects = findViewById(R.id.light_for_effect);
        initLight();
    }

    private void initLight()
    {
        BoardOperations board = new BoardOperations(getApplicationContext());
        Long lightId = board.getLight(currentBoard);
        LightOperations lightOperations = new LightOperations(getApplicationContext());
        light = lightOperations.getLight(lightId);
    }

    private void displayBoard()
    {
        List<Long> effectIds = getEffects();
        player = new PlayerOperations();
        prepareVolumeSettings();
        initEffectsGrid();
        showEffects(effectIds);
        stopPlayer.setOnClickListener(v -> player.stopPlayer(Tags.EFFECT.value()));
        lightEffects.setOnClickListener(v -> startLightConfiguration());
    }

    private void startLightConfiguration()
    {
        Intent lightConfig = new Intent(getApplicationContext(), LightConfiguration.class);
        lightConfig.putExtra("effectBoard", true);
        startActivityForResult(lightConfig, LIGHT_CONFIGURATION);
    }

    private void initEffectsGrid()
    {
        effectsGrid.setOnItemClickListener((parent, view, position, id) ->
        {
            Track track = effects.get(position);
            track.setTag(Tags.EFFECT.value());
            track.setVolume((long) volume.getProgress());
            try
            {
                if (light != null)
                {
                    changeLight();
                }
                player.startByTag(getApplicationContext(), track);
            } catch (Exception e)
            {
                Toast
                    .makeText(getApplicationContext(), "Can´t play track: " + track.getName()
                        + " \nIf the track contains non characters like: \"?!,;\" etc., rename the file and try "
                        + "again", Toast.LENGTH_LONG)
                    .show();
            }
        });
    }

    private void changeLight()
    {
        LightOperations operations = new LightOperations(getApplicationContext());
        List<String> bulbUrls = LightConnection.getInstance().getBulbs();
        for (String url : bulbUrls)
        {
            operations.changeColor(url,
                operations.getRGBtoXY(Color.valueOf(new BigDecimal(light.getColor()).intValue())));
            operations.changeBrightness(url, light.getBrightness());
        }
    }

    private void showEffects(List<Long> effectIds)
    {
        TrackOperations operations = new TrackOperations(getApplicationContext());
        for (Long id : effectIds)
        {
            Track track = operations.get(id);
            effects.add(track);
        }
        EffectsAdapter adapter = new EffectsAdapter(getApplicationContext(), effects);
        effectsGrid.setAdapter(adapter);
    }

    private List<Long> getEffects()
    {
        EffectsDao dao = new EffectsDao(getApplicationContext());
        return dao.get(currentBoard);
    }

    private void prepareVolumeSettings()
    {
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                player.adjustVolume(volume.getProgress(), Tags.EFFECT.value());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == COLLECT_TRACKS)
            {
                {
                    addTracksToBoard(data);
                }
                refreshActivity();
            } else if (requestCode == LIGHT_CONFIGURATION)
            {
                addLightToBoard(data);
            }
        }
    }

    private void addLightToBoard(Intent data)
    {
        light = data.getParcelableExtra("light");
        LightOperations operations = new LightOperations(getApplicationContext());
        if (light != null)
        {
            Long lightId = operations.createLight(light);
            BoardOperations boards = new BoardOperations(getApplicationContext());
            boards.addLightToBoard(currentBoard, lightId);
        }
    }

    private void addTracksToBoard(Intent data)
    {
        Toast
            .makeText(getApplicationContext(),
                "If you selected a large directory, please wait a moment. It could take a while...", Toast.LENGTH_LONG)
            .show();
        TrackOperations track = new TrackOperations(getApplicationContext());
        List<Track> effects =
            data.getStringArrayListExtra("effects").stream().map(track::createTrack).collect(Collectors.toList());
        BoardOperations operations = new BoardOperations(getApplicationContext());
        operations.referenceEffectsToBoard(currentBoard, effects);
    }

    private void refreshActivity()
    {
        finish();
        Intent intent = new Intent(getApplicationContext(), this.getClass());
        intent.putExtra("boardId", currentBoard);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        if (player != null)
        {
            player.stopPlayer(Tags.EFFECT.value());
        }
        goBackToBoardsOverview();
    }

    private void goBackToBoardsOverview()
    {
        Intent boards = new Intent(getApplicationContext(), BoardsView.class);
        boards.putExtra("boardId", currentBoard);
        startActivity(boards);
        finish();
    }
}