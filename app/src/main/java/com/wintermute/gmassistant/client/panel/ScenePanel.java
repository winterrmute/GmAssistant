package com.wintermute.gmassistant.client.panel;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.SceneAdapter;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dto.Scene;

import java.util.ArrayList;

/**
 * Allows the user to manage all scenes.
 */
public class ScenePanel extends AppCompatActivity
{

    private ListView sceneView;
    private ArrayList<Scene> allScenes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_panel);

        showScenes();

        sceneView.setOnItemClickListener((parent, view, position, id) -> {
            Log.e("Id :", allScenes.get(position).getId());
            Log.e("", allScenes.get(position).getName());
            Log.e("Next track: ", allScenes.get(position).getNextTrack());
            Log.e("Light", allScenes.get(position).getLight());
        });

        sceneView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            handleDialog(allScenes.get(position).getName(), allScenes.get(position).getId());
            return true;
        });
    }

    private void handleDialog(String name, String id)
    {
        SceneDao dao = new SceneDao(this);
        AlertDialog.Builder dialog = new AlertDialog.Builder(ScenePanel.this);
        dialog.setTitle(name);
        String[] opts = {"DELETE"};
        dialog.setItems(opts, (opt, which) ->
        {
            opt.dismiss();
            if (which == 0)
            {
                dao.deleteById(id);
                showScenes();
            }
        });
        dialog.show();
    }

    /**
     * Show all scenes as listView
     */
    private void showScenes()
    {
        SceneDao dao = new SceneDao(this);
        allScenes = dao.getAll();
        SceneAdapter sceneAdapter = new SceneAdapter(this, allScenes);
        sceneView = findViewById(R.id.scene_view);
        sceneView.setAdapter(sceneAdapter);
    }
}
