package com.wintermute.gmassistant.hue;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.hue.model.CustomRequest;
import com.wintermute.gmassistant.operations.LightConfigOperations;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_bridge);

        ipAddress = findViewById(R.id.ip_input);

        instructions = findViewById(R.id.pairing_result);
        connectionStatus = findViewById(R.id.connection_status);
        connectionStatus.setVisibility(View.GONE);

        Button testConnection = findViewById(R.id.test_connection);
        testConnection.setOnClickListener(v -> registerDevice());
    }

    private void registerDevice()
    {
        String url = "http://" + ipAddress.getText().toString() + "/api/";
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject req = null;
        try
        {
            req = new JSONObject("{\"devicetype\":\"gm_assistant#gm_assistant\"}");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        CustomRequest r = new CustomRequest(Request.Method.POST, url, req, response ->
        {
            retrieve(getResponse(response));
        }, error -> retrieve(null));
        requestQueue.add(r);
        instructions.setText(R.string.pending);
    }

    private void retrieve(JSONObject rsp)
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

            try
            {
                String username = rsp.getJSONObject("success").get("username").toString();
                LightConfigOperations operations = new LightConfigOperations(getApplicationContext());
                operations.storeConfig(ipAddress.getText().toString(), username);
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private JSONObject getResponse(JSONArray response)
    {
        try
        {
            return response.getJSONObject(0);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
