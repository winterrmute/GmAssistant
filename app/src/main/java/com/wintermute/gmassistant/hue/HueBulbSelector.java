package com.wintermute.gmassistant.hue;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.hue.model.HueBulb;
import com.wintermute.gmassistant.hue.model.HueUser;
import com.wintermute.gmassistant.operations.LightConfigOperations;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_bulb_selector);

        bulbsView = findViewById(R.id.bulbs);
        checkConnection();

        bulbsView.setOnItemClickListener((parent, view, position, id) ->
        {
            if (!selected.containsKey(bulbList.get(position).getName()))
            {
                selected.put(bulbList.get(position).getName(), bulbList.get(position));
            } else
            {
                selected.remove(bulbList.get(position).getName());
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
        List<String> bulbs = bulbList.stream().map(HueBulb::getName).collect(Collectors.toList());
        ArrayAdapter<String> adapter =
            new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, bulbs);
        bulbsView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        bulbsView.setAdapter(adapter);
    }

    private void checkConnection()
    {
        LightConfigOperations operations = new LightConfigOperations(getApplicationContext());
        HueUser bridge = operations.getBridge();
        String url = "http://" + bridge.getIp() + "/api/" + bridge.getUsername() + "/lights";
        ApiCaller.getInstance().makeCall(getApplicationContext(), Request.Method.GET, url, "{}", getCallbackListener());
    }

    private void listBulbs(JSONObject response)
    {
        JsonObject rsp = (JsonObject) JsonParser.parseString(response.toString());
        for (Map.Entry<String, JsonElement> entry : rsp.entrySet())
        {
            String bulbName = entry.getValue().getAsJsonObject().get("name").toString().replace("\"", "");
            String bulbType = entry.getValue().getAsJsonObject().get("productname").toString().replace("\"", "");
            bulbList.add(new HueBulb(bulbName, bulbType, false));
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
