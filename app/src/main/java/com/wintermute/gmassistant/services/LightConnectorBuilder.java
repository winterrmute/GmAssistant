package com.wintermute.gmassistant.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.android.volley.Request;
import com.wintermute.gmassistant.hue.ApiCaller;
import com.wintermute.gmassistant.hue.CallbackListener;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.operations.LightConfigOperations;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Checks and connects to philips hue on start.
 */
public class LightConnectorBuilder extends Service
{
    private HueBridge bridge;

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        LightConfigOperations operations = new LightConfigOperations(getApplicationContext());
        //        bridge = operations.getActiveBridge();
        try {
            bridge = operations.getBridges().get(0);
            if (bridge != null) {
                String url = "http://" + bridge.getIp() + "/api/" + bridge.getUsername() + "/lights";
                ApiCaller
                    .getInstance()
                    .makeCustomCall(getApplicationContext(), Request.Method.GET, url,
                        "{\"devicetype\":\"gm_assistant#gm_assistant\"}", getCallbackListener());
            }

        } catch (NullPointerException ignored){

        }
        return Service.START_NOT_STICKY;
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
                    }
                } catch (JSONException e)
                {
                    Toast
                        .makeText(getApplicationContext(), "Could not connect to philips hue!", Toast.LENGTH_LONG)
                        .show();
                    e.printStackTrace();
                }
                LightConnection.getInstance().init(getApplicationContext(), bridge);
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
}
