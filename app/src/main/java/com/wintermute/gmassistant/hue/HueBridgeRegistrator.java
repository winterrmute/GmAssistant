package com.wintermute.gmassistant.hue;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.wintermute.gmassistant.GmAssistant;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.hue.model.HueUser;
import com.wintermute.gmassistant.operations.LightConfigOperations;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Registers the device by the bridge
 *
 * @author wintermute
 */
public class HueBridgeRegistrator extends AppCompatActivity
{
    private ImageView connectionStatus;
    private EditText ipAddress;
    private TextView instructions;
    private Button pairBulbs;
    private Button testConnection;
    LightConfigOperations operations;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_bridge);

        ipAddress = findViewById(R.id.ip_input);
        pairBulbs = findViewById(R.id.pair_bulbs);
        pairBulbs.setVisibility(View.GONE);
        pairBulbs.setOnClickListener(v -> startActivity(new Intent(HueBridgeRegistrator.this, HueBulbSelector.class)));

        instructions = findViewById(R.id.pairing_result);
        connectionStatus = findViewById(R.id.connection_status);
        connectionStatus.setVisibility(View.GONE);

        operations = new LightConfigOperations(getApplicationContext());
        checkRegistration();

        testConnection = findViewById(R.id.test_connection);
        testConnection.setOnClickListener(v -> register());
    }

    private void checkRegistration()
    {
        HueUser bridge = operations.getBridge();
        if (bridge != null)
        {
            testConnection();
        }
    }

    private void testConnection()
    {
        operations = new LightConfigOperations(getApplicationContext());
        HueUser bridge = operations.getBridge();
        String url = "http://" + bridge.getIp() + "/api/" + bridge.getUsername() + "/lights";
        ApiCaller.getInstance().makeCall(getApplicationContext(), Request.Method.GET, url, "{}", getCallbackListener());
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
                    pairBulbs.setVisibility(View.VISIBLE);
                    testConnection.setVisibility(View.GONE);
                    connectionStatus.setVisibility(View.VISIBLE);
                    connectionStatus.setImageResource(R.drawable.success);
                    instructions.setText("Hue bridge is connected!");
                }
            }

            @Override
            public void onError(String msg)
            {
                retrieveConnectionStatus(null);
                Log.e("tag", msg);
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
            return;
        }

        if (rsp.has("error"))
        {
            connectionStatus.setImageResource(R.drawable.warning);
            instructions.setText(R.string.permission_denied);
            return;
        }

        if (rsp.has("success"))
        {
            connectionStatus.setImageResource(R.drawable.success);
            instructions.setText(R.string.connection_successful);
            pairBulbs.setVisibility(View.VISIBLE);

            try
            {
                String username = rsp.getJSONObject("success").get("username").toString();
                operations = new LightConfigOperations(getApplicationContext());
                operations.storeConfig(ipAddress.getText().toString(), username);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
