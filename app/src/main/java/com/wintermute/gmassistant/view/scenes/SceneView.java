package com.wintermute.gmassistant.view.scenes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.SceneAdapter;
import com.wintermute.gmassistant.dialogs.ListDialog;
import com.wintermute.gmassistant.view.model.Scene;
import com.wintermute.gmassistant.operations.SceneOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Allows the user to manage all scenes.
 *
 * @author wintermute
 */
public class SceneView extends AppCompatActivity
{

    private ListView sceneView;
    private Scene scene;
    private SceneOperations operations;
    private List<Scene> allScenes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_panel);

        allScenes = new ArrayList<>();
        operations = new SceneOperations(getApplicationContext());
        showScenes();

        sceneView.setOnItemClickListener(
            (parent, view, position, id) -> operations.startScene(allScenes.get(position)));

        sceneView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            scene = allScenes.get(position);
            Intent dialog = new Intent(SceneView.this, ListDialog.class);
            dialog.putStringArrayListExtra("opts", new ArrayList<>(Collections.singletonList("delete")));
            startActivityForResult(dialog, 2);
            return true;
        });

        Button addScene = findViewById(R.id.add_scene);
        addScene.setOnClickListener((v) -> addScene());
    }

    /**
     * Open scene config activity with editing flag.
     */
    private void addScene()
    {
        Intent sceneConfig = new Intent(SceneView.this, SceneConfig.class);
        startActivityForResult(sceneConfig, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 2)
            {
                String action = data.getStringExtra("action");
                if ("delete".equals(action))
                {
                    operations.deleteElement(scene);
                }
            }
            showScenes();
        }
    }

    /**
     * Show all scenes as listView
     */
    private void showScenes()
    {
        allScenes = operations.loadViewElements();
        SceneAdapter sceneAdapter = new SceneAdapter(this, allScenes);
        sceneView = findViewById(R.id.scene_view);
        sceneView.setAdapter(sceneAdapter);
    }
}
