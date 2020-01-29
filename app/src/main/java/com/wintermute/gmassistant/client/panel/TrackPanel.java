package com.wintermute.gmassistant.client.panel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.TrackAdapter;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.model.Track;
import com.wintermute.gmassistant.dialogs.ListDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Allows the user to manage all tracks.
 */
public class TrackPanel extends AppCompatActivity
{

    private ListView trackView;
    private List<Track> allTracks;
    private String trackId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_panel);

        showAllTracks();

        trackView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            trackId = allTracks.get(position).getId();
            handleDialog();
            return true;
        });
    }

    private void showAllTracks()
    {
        trackView = findViewById(R.id.track_list);
        TrackDao dao = new TrackDao(getApplication());
        allTracks = dao.getAll();
        TrackAdapter trackAdapter = new TrackAdapter(this, allTracks);
        trackView.setAdapter(trackAdapter);
    }

    private void handleDialog()
    {
        Intent dialog = new Intent(TrackPanel.this, ListDialog.class);
        dialog.putStringArrayListExtra("opts", new ArrayList<>(Arrays.asList("DELETE")));
        startActivityForResult(dialog, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0)
        {
            TrackDao dao = new TrackDao(this);
            dao.deleteById(trackId);
        }
        showAllTracks();
    }
}
