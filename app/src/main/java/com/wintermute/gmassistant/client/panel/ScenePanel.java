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
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dto.Scene;
import com.wintermute.gmassistant.dialogs.ListDialog;
import com.wintermute.gmassistant.handlers.PlayerHandler;

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
    private ArrayList<Scene> allScenes;
    private String sceneId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_panel);

        showScenes();

        sceneView.setOnItemClickListener((parent, view, position, id) ->
        {
            PlayerHandler handler = new PlayerHandler(getBaseContext());
            handler.startPlayerByScene(allScenes.get(position).getId());
        });

        sceneView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            sceneId = allScenes.get(position).getId();
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
        sceneConfig.putExtra("sceneId", sceneId);
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
                SceneDao dao = new SceneDao(this);
                dao.deleteById(sceneId);
                PlaylistContentDao playlistContentDao = new PlaylistContentDao(this);
                playlistContentDao.deleteScene(sceneId);
            }
        }
        showScenes();
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
