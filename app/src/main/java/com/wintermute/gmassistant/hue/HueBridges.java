package com.wintermute.gmassistant.hue;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.HueBridgeAdapter;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.operations.LightConfigOperations;

import java.util.List;

public class HueBridges extends AppCompatActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_bridges);

        LightConfigOperations operations = new LightConfigOperations(getApplicationContext());
        List<HueBridge> bridges = operations.getBridges();

        ListView bridgeView = findViewById(R.id.bridges);
        if (bridges.size() > 0){
            findViewById(R.id.no_devices_configured).setVisibility(View.GONE);
            listBridges(bridgeView, bridges);

        } else {
            bridgeView.setVisibility(View.GONE);
        }

        Button pairBridge = findViewById(R.id.bridge_pairing);
        pairBridge.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), HueBridgeRegistrator.class)));
    }

    private void listBridges(ListView view, List<HueBridge> bridges)
    {
        view.setAdapter(new HueBridgeAdapter(getApplicationContext(), bridges));
        initClickListener(view, bridges);
    }

    void initClickListener(ListView view, List<HueBridge> bridges){
        view.setOnItemClickListener((parent, view1, position, id) ->
        {
            Intent bridgeView = new Intent(getApplicationContext(), HueBridgeRegistrator.class);
            bridgeView.putExtra("bridge", bridges.get(position));
            startActivity(bridgeView);
        });
    }
}
