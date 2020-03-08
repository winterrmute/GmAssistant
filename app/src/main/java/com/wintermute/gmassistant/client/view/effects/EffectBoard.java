package com.wintermute.gmassistant.client.view.effects;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.EffectsAdapter;
import com.wintermute.gmassistant.database.dao.EffectsDao;
import com.wintermute.gmassistant.database.model.Tags;
import com.wintermute.gmassistant.model.Track;
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.operations.TrackOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Board containing effect sounds.
 */
public class EffectBoard extends AppCompatActivity
{

    private List<Track> effects;
    private GridView effectsGrid;
    private SeekBar volume;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect_board);

        PlayerOperations player = new PlayerOperations();

        effects = new ArrayList<>();
        effectsGrid = findViewById(R.id.effect_grid);

        ImageButton stopPlayer = findViewById(R.id.stop_player);
        stopPlayer.setOnClickListener(v ->
        {
            player.stopPlayer(Tags.EFFECT.value());
        });

        volume = findViewById(R.id.volume);
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

        listItems();

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
                            + " \nIf the track contains non characters like: \"?!,;\" etc., rename the file and try again",
                        Toast.LENGTH_LONG)
                    .show();
            }
        });
    }

    private void listItems()
    {
        EffectsDao dao = new EffectsDao(getApplicationContext());
        Long groupId = getIntent().getLongExtra("groupId", -1L);
        List<Long> effectIds = dao.get(groupId) != null ? dao.get(groupId) : new ArrayList<>();

        TrackOperations operations = new TrackOperations(getApplicationContext());
        for (Long id : effectIds)
        {
            Track track = operations.get(id);
            effects.add(track);
        }

        EffectsAdapter adapter = new EffectsAdapter(getApplicationContext(), effects);
        effectsGrid.setAdapter(adapter);
    }
}