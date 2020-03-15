package com.wintermute.gmassistant.view.scenes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.SceneAdapter;
import com.wintermute.gmassistant.dialogs.ListDialog;
import com.wintermute.gmassistant.hue.ApiCaller;
import com.wintermute.gmassistant.hue.CallbackListener;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.operations.LightConfigOperations;
import com.wintermute.gmassistant.operations.SceneOperations;
import com.wintermute.gmassistant.services.LightConnection;
import com.wintermute.gmassistant.view.model.Scene;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Allows the user to manage all scenes.
 *
 * @author wintermute
 */
public class SceneBoard extends AppCompatActivity
{

    public static final int CREATE_SCENE = 1;
    public static final int DELETE_SCENE = 2;
    private ListView sceneView;
    private Scene scene;
    private HueBridge bridge;
    private SceneOperations operations;
    private List<Scene> scenesAssignedToBoard;
    private SceneAdapter sceneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_panel);
        displayBoard();
        connectLights();

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

        Button addScene = findViewById(R.id.add_scene);
        addScene.setOnClickListener((v) -> addScene());
    }

    private void displayBoard() {
        operations = operations == null ? new SceneOperations(getApplicationContext()) : operations;
        scenesAssignedToBoard = operations.getScenesAssignedToBoard(getIntent().getLongExtra("boardId", -1L));
        sceneAdapter = sceneAdapter == null ? new SceneAdapter(getApplicationContext(), scenesAssignedToBoard) : sceneAdapter;
        sceneAdapter.updateDisplayedElements(scenesAssignedToBoard);
        sceneView = findViewById(R.id.scene_view);
        sceneView.setAdapter(sceneAdapter);
    }

    private void connectLights()
    {
        LightConfigOperations operations = new LightConfigOperations(getApplicationContext());
        //        bridge = operations.getActiveBridge();
        try
        {
            bridge = operations.getBridges().get(0);
            if (bridge != null)
            {
                String url = "http://" + bridge.getIp() + "/api/" + bridge.getUsername() + "/lights";
                ApiCaller
                    .getInstance()
                    .makeCustomCall(getApplicationContext(), Request.Method.GET, url,
                        "{\"devicetype\":\"gm_assistant#gm_assistant\"}", getCallbackListener());
            }
        } catch (NullPointerException ignored)
        {

        }
    }

    @NotNull
    private CallbackListener getCallbackListener()
    {
        return new CallbackListener()
        {
            private final Toast noConnection =
                Toast.makeText(getApplicationContext(), "Could not connect to philips hue!", Toast.LENGTH_LONG);

            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    if (response.getJSONObject(0).has("error"))
                    {
                        noConnection.show();
                        return;
                    }
                    LightConnection.getInstance().init(getApplicationContext(), bridge);
                } catch (JSONException e)
                {
                    noConnection.show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(JSONObject response)
            {
                //do nothing
            }

            @Override
            public void onError(String msg)
            {
                noConnection.show();
            }
        };
    }

    /**
     * Open scene config activity with editing flag.
     */
    private void addScene()
    {
        Intent sceneConfig = new Intent(SceneBoard.this, SceneConfig.class);
        sceneConfig.putExtra("boardId", getIntent().getLongExtra("boardId", -1L));
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
            displayBoard();
        }
    }
}
