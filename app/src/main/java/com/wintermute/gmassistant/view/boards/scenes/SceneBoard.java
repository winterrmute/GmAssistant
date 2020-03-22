package com.wintermute.gmassistant.view.boards.scenes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.SceneAdapter;
import com.wintermute.gmassistant.dialogs.ListDialog;
import com.wintermute.gmassistant.operations.SceneOperations;
import com.wintermute.gmassistant.view.boards.BoardsView;
import com.wintermute.gmassistant.view.model.Scene;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Allows the user to manage all scenes.
 *
 * @author wintermute
 */
public class SceneBoard extends AppCompatActivity
{

    public static final int CREATE_SCENE = 1;
    public static final int DELETE_SCENE = 2;
    private Scene scene;
    private SceneOperations operations;
    private List<Scene> scenesAssignedToBoard;
    private Long currentBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_board);
        currentBoard = getIntent().getLongExtra("boardId", -1L);

        displayBoardContent();

        Button addScene = findViewById(R.id.add_scene);
        addScene.setOnClickListener((v) -> addScene());

        Button randomScene = findViewById(R.id.random_scene);
        randomScene.setOnClickListener((v) -> randomScene());
    }

    private void randomScene()
    {
        Random random = new Random();
        operations.startScene(scenesAssignedToBoard.get(random.nextInt(scenesAssignedToBoard.size())));
    }

    private void displayBoardContent()
    {
        operations = operations == null ? new SceneOperations(getApplicationContext()) : operations;
        scenesAssignedToBoard = operations.getScenesAssignedToBoard(currentBoard);
        initSceneView();
    }

    private void initSceneView()
    {
        ListView sceneView = findViewById(R.id.scene_view);
        SceneAdapter sceneAdapter = new SceneAdapter(getApplicationContext(), scenesAssignedToBoard);
        sceneView.setAdapter(sceneAdapter);
        //        sceneAdapter.updateDisplayedElements(scenesAssignedToBoard);

        sceneView.setOnItemClickListener(
            (parent, view, position, id) -> operations.startScene(scenesAssignedToBoard.get(position)));

        sceneView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            scene = scenesAssignedToBoard.get(position);
            Intent dialog = new Intent(SceneBoard.this, ListDialog.class);
            dialog.putStringArrayListExtra("opts", new ArrayList<>(Collections.singletonList("delete")));
            startActivityForResult(dialog, DELETE_SCENE);
            return true;
        });
    }

    /**
     * Open scene config activity with editing flag.
     */
    private void addScene()
    {
        Intent sceneConfig = new Intent(SceneBoard.this, SceneConfig.class);
        sceneConfig.putExtra("boardId", currentBoard);
        startActivityForResult(sceneConfig, CREATE_SCENE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == DELETE_SCENE)
            {
                String action = data.getStringExtra("action");
                if ("delete".equals(action))
                {
                    operations.deleteElement(scene);
                }
            }
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public void onBackPressed()
    {
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
