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
import com.wintermute.gmassistant.services.LightConnection;
import com.wintermute.gmassistant.view.model.Scene;
import com.wintermute.gmassistant.operations.SceneOperations;
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
public class SceneView extends AppCompatActivity
{

    private ListView sceneView;
    private Scene scene;
    private HueBridge bridge;
    private SceneOperations operations;
    private List<Scene> allScenes;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_panel);

        connectLights();

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
            @Override
            public void onResponse(JSONArray response)
            {
                try
                {
                    if (response.getJSONObject(0).has("error"))
                    {
                        Toast
                            .makeText(getApplicationContext(), "Could not connect to philips hue!", Toast.LENGTH_LONG)
                            .show();
                        return;
                    }
                    LightConnection.getInstance().init(getApplicationContext(), bridge);
                } catch (JSONException e)
                {
                    Toast
                        .makeText(getApplicationContext(), "Could not connect to philips hue!", Toast.LENGTH_LONG)
                        .show();
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
                Toast.makeText(getApplicationContext(), "Could not connect to philips hue!", Toast.LENGTH_LONG).show();
            }
        };
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
