package com.wintermute.gmassistant;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.wintermute.gmassistant.hue.ApiCaller;
import com.wintermute.gmassistant.hue.CallbackListener;
import com.wintermute.gmassistant.hue.HueBridges;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.operations.LightConfigOperations;
import com.wintermute.gmassistant.services.LightConnection;
import com.wintermute.gmassistant.view.boards.BoardsView;
import com.wintermute.gmassistant.view.library.LibraryContent;
import com.wintermute.gmassistant.view.playlist.PlaylistView;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Startup activity. Provides Game masters panel.
 */
public class GmAssistant extends AppCompatActivity
{
    private HueBridge bridge;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gm_assistant);

        grantUserPermission();

        Button playlistView = findViewById(R.id.manage_playlists);
        playlistView.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, PlaylistView.class)));

        Button audioFileView = findViewById(R.id.manage_tracks);
        audioFileView.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, LibraryContent.class)));

        Button scenesBoard = findViewById(R.id.manage_scenes);
        scenesBoard.setOnClickListener(
            l -> startActivity(new Intent(GmAssistant.this, BoardsView.class).putExtra("boardCategory", "scenes")));

        Button hueManagementView = findViewById(R.id.manage_lights);
        hueManagementView.setOnClickListener(l -> startActivity(new Intent(GmAssistant.this, HueBridges.class)));

        Button effectBoards = findViewById(R.id.effect_board);
        effectBoards.setOnClickListener(
            l -> startActivity(new Intent(GmAssistant.this, BoardsView.class).putExtra("boardCategory", "effects")));

        connectLights();
    }

    private void connectLights()
    {

        ApiCaller apiCall = new ApiCaller();
        LightConfigOperations operations = new LightConfigOperations(getApplicationContext());
        //        bridge = operations.getActiveBridge();
        try
        {
            bridge = operations.getBridges().get(0);
            if (bridge != null)
            {
                String url = "http://" + bridge.getIp() + "/api/" + bridge.getUsername() + "/lights";
                apiCall.makeCustomCall(getApplicationContext(), Request.Method.GET, url,
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
     * Grants permissions to the application to access the storage.
     */
    void grantUserPermission()
    {
        String[] permissions =
            {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.MEDIA_CONTENT_CONTROL,
             android.Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.INTERNET};
        for (String permission : permissions)
        {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED)
            {
                shouldShowRequestPermissionRationale(permission);
                requestPermissions(new String[] {permission}, 0);
            }
        }
    }
}
