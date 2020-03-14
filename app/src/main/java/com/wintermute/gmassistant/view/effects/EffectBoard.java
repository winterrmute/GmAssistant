package com.wintermute.gmassistant.view.effects;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.operations.TrackOperations;
import com.wintermute.gmassistant.view.StorageBrowser;
import com.wintermute.gmassistant.view.model.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Board containing effect sounds.
 */
public class EffectBoard extends AppCompatActivity
{

    private static final int COLLECT_TRACKS = 1;
    private List<Track> effects;
    private GridView effectsGrid;
    private SeekBar volume;
    private PlayerOperations player;
    private ImageButton stopPlayer;
    private Button addEffects;
    private EffectsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect_board);
        initComponents();
        displayBoard();
    }

    private void initComponents()
    {
        effects = new ArrayList<>();
        volume = findViewById(R.id.volume);
        effectsGrid = findViewById(R.id.effect_grid);
        stopPlayer = findViewById(R.id.stop_player);
        addEffects = findViewById(R.id.add_effects);
    }

    private void displayBoard()
    {
        List<Long> effectIds = getEffects();
        if (effectIds.size() > 0)
        {
            addEffects.setVisibility(View.GONE);
            player = new PlayerOperations();
            prepareVolumeSettings();
            initEffectsGrid();
            showEffects(effectIds);
            stopPlayer.setOnClickListener(v -> player.stopPlayer(Tags.EFFECT.value()));
        } else
        {
            addEffects.setOnClickListener(
                v -> startActivityForResult(new Intent(this, StorageBrowser.class), COLLECT_TRACKS));
            findViewById(R.id.volume_label).setVisibility(View.GONE);
            stopPlayer.setVisibility(View.GONE);
            volume.setVisibility(View.GONE);
            effectsGrid.setVisibility(View.GONE);
        }
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

    private void showEffects(List<Long> effectIds)
    {
        TrackOperations operations = new TrackOperations(getApplicationContext());
        for (Long id : effectIds)
        {
            Track track = operations.get(id);
            effects.add(track);
        }
        adapter = new EffectsAdapter(getApplicationContext(), effects);
        effectsGrid.setAdapter(adapter);
    }

    private List<Long> getEffects()
    {
        EffectsDao dao = new EffectsDao(getApplicationContext());
        Long groupId = getIntent().getLongExtra("boardId", -1L);
        return dao.get(groupId);
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
            {
                Toast
                    .makeText(getApplicationContext(),
                        "If you selected a large directory, please wait a moment. It could take a while...",
                        Toast.LENGTH_LONG)
                    .show();
                TrackOperations track = new TrackOperations(getApplicationContext());
                List<Track> effects = data
                    .getStringArrayListExtra("effects")
                    .stream()
                    .map(track::createTrack)
                    .collect(Collectors.toList());
                BoardOperations operations = new BoardOperations(getApplicationContext());
                operations.referenceEffectsToBoard(getIntent().getLongExtra("boardId", -1L), effects);
                updateView(effects);
            }
        }
    }

    private void updateView(List<Track> newContent)
    {
        if (adapter == null)
        {
            adapter = new EffectsAdapter(getApplicationContext(), newContent);
        }
        adapter.updateDisplayedElements(newContent);
        finish();
        startActivity(getIntent());
    }

    @Override
    public void onBackPressed()
    {
        if (player != null)
        {
            player.stopPlayer(Tags.EFFECT.value());
        }
        finish();
    }
}