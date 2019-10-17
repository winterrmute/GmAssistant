package com.wintermute.gmassistant.client.panel;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.SceneAdapter;
import com.wintermute.gmassistant.config.SceneConfig;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dto.Scene;
import com.wintermute.gmassistant.handler.PlayerHandler;

import java.util.ArrayList;

/**
 * Allows the user to manage all scenes.
 *
 * @author wintermute
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

        sceneView.setOnItemClickListener((parent, view, position, id) ->
        {
            if (allScenes.get(position).getStartingTrack() != null) {
                PlayerHandler handler = new PlayerHandler(getBaseContext());
                handler.startPlayerByScene(allScenes.get(position).getId());
            }
        });

        sceneView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            handleDialog(allScenes.get(position).getName(), allScenes.get(position).getId());
            return true;
        });

        Button addScene = findViewById(R.id.add_scene);
        addScene.setOnClickListener((v) -> {
            Intent sceneConfig = new Intent(ScenePanel.this, SceneConfig.class);
            startActivityForResult(sceneConfig, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1)
        {
            showScenes();
        }
    }

    /**
     * Handle dialog on long click.
     *
     * @param name of selected element
     * @param id of selected element
     */
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
