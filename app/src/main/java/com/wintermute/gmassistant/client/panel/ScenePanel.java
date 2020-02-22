package com.wintermute.gmassistant.client.panel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.SceneAdapter;
import com.wintermute.gmassistant.config.SceneConfig;
import com.wintermute.gmassistant.database.dao.PlaylistContentDao;
import com.wintermute.gmassistant.dialogs.ListDialog;
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.operator.SceneOperations;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Allows the user to manage all scenes.
 *
 * @author wintermute
 */
public class ScenePanel extends AppCompatActivity
{

    private ListView sceneView;
    private Scene scene;
    private SceneOperations operations;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_panel);

        operations = new SceneOperations(getApplicationContext());
        ArrayList<Scene> allScenes = operations.loadViewElements();
        showScenes();

        sceneView.setOnItemClickListener(
            (parent, view, position, id) -> operations.startScene(allScenes.get(position).getId()));

        sceneView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            scene = allScenes.get(position);
            Intent dialog = new Intent(ScenePanel.this, ListDialog.class);
            dialog.putStringArrayListExtra("opts", new ArrayList<>(Arrays.asList("edit", "delete")));
            startActivityForResult(dialog, 1);
            return true;
        });

        Button addScene = findViewById(R.id.add_scene);
        addScene.setOnClickListener((v) -> startSceneConfiguration(false));
    }

    /**
     * Open scene config activity with editing flag.
     *
     * @param edit flag to refer if scene should be edited or a new one will be created.
     */
    private void startSceneConfiguration(boolean edit)
    {
        Intent sceneConfig = new Intent(ScenePanel.this, SceneConfig.class);
        sceneConfig.putExtra("edit", edit);
        sceneConfig.putExtra("sceneId", scene.getId());
        startActivityForResult(sceneConfig, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1)
        {
            String selected = data.getStringExtra("selected");

            if ("edit".equals(selected))
            {
                startSceneConfiguration(true);
            } else if ("delete".equals(selected))
            {
                operations.deleteElement(scene);
                //TODO: refactor so that the scene is independent
                PlaylistContentDao playlistContentDao = new PlaylistContentDao(this);
                playlistContentDao.deleteScene(scene.getId());
            }
        }
        showScenes();
    }

    /**
     * Show all scenes as listView
     */
    private void showScenes()
    {
        SceneAdapter sceneAdapter = new SceneAdapter(this, operations.loadViewElements());
        sceneView = findViewById(R.id.scene_view);
        sceneView.setAdapter(sceneAdapter);
    }
}
