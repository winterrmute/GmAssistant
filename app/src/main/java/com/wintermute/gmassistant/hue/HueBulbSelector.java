package com.wintermute.gmassistant.hue;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.HueBulbAdapter;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.hue.model.HueBulb;
import com.wintermute.gmassistant.operations.LightConfigOperations;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Lists selectable bulbs and allows to create bulb setting.
 *
 * @author wintermute
 */
public class HueBulbSelector extends AppCompatActivity
{

    private ListView bulbsView;
    private List<HueBulb> bulbList = new ArrayList<>();
    private Map<String, HueBulb> selected = new HashMap<>();
    private HueBridge bridge;
    private ApiCaller apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_bulb_selector);

        bulbsView = findViewById(R.id.bulbs);
        apiCall = new ApiCaller();
        checkConnection();
        createSelectableListAdapter();

        bulbsView.setOnItemClickListener((parent, view, position, id) ->
        {
            if (!selected.containsKey(bulbList.get(position).getName()))
            {
                selected.put(bulbList.get(position).getName(), bulbList.get(position));
                Toast
                    .makeText(getApplicationContext(), bulbList.get(position).getName() + " added", Toast.LENGTH_SHORT)
                    .show();
            } else
            {
                selected.remove(bulbList.get(position).getName());
                Toast
                    .makeText(getApplicationContext(), bulbList.get(position).getName() + " removed",
                        Toast.LENGTH_SHORT)
                    .show();
            }
        });

        Button addBulbs = findViewById(R.id.add_bulbs);
        addBulbs.setOnClickListener(v ->
        {
            LightConfigOperations operations = new LightConfigOperations(getApplicationContext());
            operations.addBulbs(selected);
            finish();
        });
    }

    private void createSelectableListAdapter()
    {
        HueBulbAdapter adapter = new HueBulbAdapter(getApplicationContext(), bulbList);
        bulbsView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        bulbsView.setAdapter(adapter);
    }

    private void checkConnection()
    {
        bridge = getIntent().getParcelableExtra("bridge");
        String url = "http://" + bridge.getIp() + "/api/" + bridge.getUsername() + "/lights";
        apiCall.customRequest(getApplicationContext(), Request.Method.GET, url, "{}", getCallbackListener());
    }

    private void listBulbs(JSONObject response)
    {
        JsonObject rsp = (JsonObject) JsonParser.parseString(response.toString());
        for (Map.Entry<String, JsonElement> entry : rsp.entrySet())
        {
            Long id = Long.valueOf(entry.getKey());
            String bulbName = entry.getValue().getAsJsonObject().get("name").toString().replace("\"", "");
            String bulbType = entry.getValue().getAsJsonObject().get("productname").toString().replace("\"", "");
            bulbList.add(new HueBulb(id, bulbName, bulbType, bridge.getId(), false));
        }
        createSelectableListAdapter();
    }

    private void isConnected(JSONObject rsp)
    {
        if (rsp == null)
        {
            //do something
            return;
        }

        if (rsp.has("error"))
        {
            //do something
            return;
        }
        listBulbs(rsp);
    }

    private CallbackListener getCallbackListener()
    {
        return new CallbackListener()
        {
            @Override
            public void onResponse(JSONArray response)
            {
                //do nothing
            }

            @Override
            public void onResponse(JSONObject response)
            {
                isConnected(response);
            }

            @Override
            public void onError(String msg)
            {
                Log.e("ERROR", msg);
                Toast
                    .makeText(getApplicationContext(), "Could not recive response from philips hue bridge",
                        Toast.LENGTH_LONG)
                    .show();
                isConnected(null);
            }
        };
    }
}
