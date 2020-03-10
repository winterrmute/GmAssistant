package com.wintermute.gmassistant.hue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.operations.LightConfigOperations;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Registers the device by the bridge
 *
 * @author wintermute
 */
public class HueBridgeRegistrator extends AppCompatActivity
{
    private ImageView connectionStatus;
    private EditText name;
    private EditText ipAddress;
    private TextView instructions;
    private Button pairBulbs;
    private Button registerDevice;
    private HueBridge bridge;
    private LightConfigOperations operations;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_bridge);

        init();

        pairBulbs.setOnClickListener(v -> startActivity(
            new Intent(HueBridgeRegistrator.this, HueBulbSelector.class).putExtra("bridge", bridge)));
        registerDevice.setOnClickListener(v ->
        {
            if ("".equals(name.getText().toString()))
            {
                Toast
                    .makeText(getApplicationContext(), "Please set bridge name and try again!", Toast.LENGTH_LONG)
                    .show();
            } else
            {
                register();
            }
        });
    }

    void init()
    {
        operations = new LightConfigOperations(getApplicationContext());
        name = findViewById(R.id.bridge_name_input);
        ipAddress = findViewById(R.id.ip_input);
        pairBulbs = findViewById(R.id.pair_bulbs);
        registerDevice = findViewById(R.id.test_connection);
        instructions = findViewById(R.id.pairing_result);
        connectionStatus = findViewById(R.id.connection_status);
        pairBulbs.setVisibility(View.GONE);
        connectionStatus.setVisibility(View.GONE);

        if (getIntent().hasExtra("bridge"))
        {
            instructions.setText(R.string.check_connection);
            registerDevice.setVisibility(View.GONE);
            bridge = getIntent().getParcelableExtra("bridge");
            name.setText(bridge.getName());
            ipAddress.setText(bridge.getIp());
            testConnection(bridge);
        }
    }

    private void testConnection(HueBridge bridge)
    {
        String url = "http://" + bridge.getIp() + "/api/" + bridge.getUsername() + "/lights";
        ApiCaller
            .getInstance()
            .makeCustomCall(getApplicationContext(), Request.Method.GET, url, "{}", getCallbackListener());
    }

    public void register()
    {
        String url = "http://" + ipAddress.getText().toString() + "/api";
        ApiCaller
            .getInstance()
            .makeCustomCall(getApplicationContext(), Request.Method.POST, url,
                "{\"devicetype\":\"gm_assistant#gm_assistant\"}", getCallbackListener());
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
                    retrieveConnectionStatus(response.getJSONObject(0));
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponse(JSONObject response)
            {
                if (!response.has("error"))
                {
                    registerDevice.setVisibility(View.GONE);
                    pairBulbs.setVisibility(View.VISIBLE);
                    connectionStatus.setVisibility(View.VISIBLE);
                    connectionStatus.setImageResource(R.drawable.success);
                    instructions.setText(R.string.connection_successful);
                }
            }

            @Override
            public void onError(String msg)
            {
                retrieveConnectionStatus(null);
                Log.e("ERROR", msg);
            }
        };
    }

    private void retrieveConnectionStatus(JSONObject rsp)
    {
        connectionStatus.setVisibility(View.VISIBLE);
        if (rsp == null)
        {
            connectionStatus.setImageResource(R.drawable.error);
            instructions.setText(R.string.bridge_not_found);
            registerDevice.setVisibility(View.VISIBLE);
            return;
        }

        if (rsp.has("error"))
        {
            connectionStatus.setImageResource(R.drawable.warning);
            instructions.setText(R.string.permission_denied);
            registerDevice.setVisibility(View.VISIBLE);
            return;
        }

        if (rsp.has("success") || hasState(rsp))
        {
            connectionStatus.setImageResource(R.drawable.success);
            instructions.setText(R.string.connection_successful);
            pairBulbs.setVisibility(View.VISIBLE);
            try
            {
                String username = rsp.getJSONObject("success").get("username").toString();
                operations = new LightConfigOperations(getApplicationContext());
                operations.storeConfig(name.getText().toString(), ipAddress.getText().toString(), username);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private boolean hasState(JSONObject response)
    {
        JsonObject rsp = (JsonObject) JsonParser.parseString(response.toString());
        for (Map.Entry<String, JsonElement> entry : rsp.entrySet())
        {
            if (entry.getValue().getAsJsonObject().has("state"))
            {
                return true;
            }
        }
        return false;
    }
}
