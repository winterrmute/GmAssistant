package com.wintermute.gmassistant.client.panel;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.TrackAdapter;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Track;

import java.util.List;

/**
 * Allows the user to manage all tracks.
 */
public class TrackPanel extends AppCompatActivity
{

    private ListView trackView;
    private List<Track> allTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_panel);

        showAllTracks();

        trackView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            handleDialog(allTracks.get(position).getName(), allTracks.get(position).getId());
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

    private void handleDialog(String name, String id)
    {
        TrackDao dao = new TrackDao(this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(TrackPanel.this);
        dialog.setTitle(name);
        String[] opts = {"DELETE"};
        dialog.setItems(opts, (opt, which) ->
        {
            opt.dismiss();
            if (which == 0)
            {
                dao.deleteById(id);
                showAllTracks();
            }
        });
        dialog.show();
    }
}
