package com.wintermute.gmassistant.hue;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.HueBridgeAdapter;
import com.wintermute.gmassistant.dialogs.ListDialog;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.operations.LightConfigOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Manages existing hue bridges
 *
 * @author wintermute
 */
public class HueBridges extends AppCompatActivity
{

    public static final int ADD_BRIDGE = 0;
    private HueBridge bridge;
    private HueBridgeAdapter adapter;
    private List<HueBridge> bridges;
    private ListView bridgeView;
    private LightConfigOperations operations;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_bridges);

        operations = new LightConfigOperations(getApplicationContext());
        bridges = operations.getBridges();

        bridgeView = findViewById(R.id.bridges);
        if (bridges.size() > 0)
        {
            findViewById(R.id.no_devices_configured).setVisibility(View.GONE);
            listBridges(bridgeView);
        } else
        {
            bridgeView.setVisibility(View.GONE);
        }

        Button pairBridge = findViewById(R.id.bridge_pairing);
        pairBridge.setOnClickListener(
            v -> startActivityForResult(new Intent(getApplicationContext(), HueBridgeRegistrator.class), ADD_BRIDGE));
    }

    private void listBridges(ListView view)
    {
        adapter = new HueBridgeAdapter(getApplicationContext(), bridges);
        view.setAdapter(adapter);
        initClickListener(view);
    }

    void initClickListener(ListView listView)
    {
        listView.setOnItemClickListener((parent, view1, position, id) ->
        {
            Intent bridgeView = new Intent(getApplicationContext(), HueBridgeRegistrator.class);
            bridgeView.putExtra("bridge", bridges.get(position));
            startActivityForResult(bridgeView, ADD_BRIDGE);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            bridge = bridges.get(position);
            Intent dialog = new Intent(getApplicationContext(), ListDialog.class);
            dialog.putStringArrayListExtra("opts", new ArrayList<>(Collections.singletonList("delete")));
            startActivityForResult(dialog, 2);
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        LightConfigOperations operations = new LightConfigOperations(getApplicationContext());
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 0)
            {
                updateListView();
            }
            if (requestCode == 2)
            {
                String action = data.getStringExtra("action");
                if ("delete".equals(action))
                {
                    operations.delete(bridge);
                }
            }
            updateListView();
        }
    }

    private void updateListView()
    {
        adapter.update(operations.getBridges());
        bridgeView.setAdapter(adapter);
    }
}
