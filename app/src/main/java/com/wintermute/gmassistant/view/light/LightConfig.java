package com.wintermute.gmassistant.view.light;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.handlers.LightHandler;
import com.wintermute.gmassistant.hue.ApiCaller;
import com.wintermute.gmassistant.hue.CallbackListener;
import com.wintermute.gmassistant.operations.LightConfigOperations;
import com.wintermute.gmassistant.operations.LightOperations;
import com.wintermute.gmassistant.services.LightConnection;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class LightConfig extends AppCompatActivity
{

    private int picked;
    private int brightness;
    private LightConfigOperations lightConfig;
    private LightOperations light;
    private LightHandler lightHandler;

    public LightConfig()
    {
        lightConfig = new LightConfigOperations(getApplicationContext());
        light = new LightOperations(getApplicationContext());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_handler);

        Button btn = findViewById(R.id.light_submit);
        btn.setOnClickListener(v ->
        {
            storeLight();

            setResult(RESULT_OK);
            lightHandler.setLight(true);
            finish();
        });

        ColorPickerView colorPickerView = findViewById(R.id.color_picker);
        colorPickerView.setColorListener((ColorListener) (color, fromUser) ->
        {
            light.getRGBtoXY(Color.valueOf(new BigDecimal(color).intValue()));

            //            lightHandler =
            //                new LightHandler(getBaseContext(), Color.valueOf(new BigDecimal(color).intValue()),
            //                brightness);
            //            lightHandler.setLight(false);
        });

        SeekBar seekbar = findViewById(R.id.brightness_bar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                ApiCaller
                    .getInstance()
                    .makeCustomCall(getApplicationContext(), Request.Method.PUT, LightConnection.getInstance().getUrl(),
                        "{}", getCallbackListener());

                brightness = progress;
                lightHandler =
                    new LightHandler(getBaseContext(), Color.valueOf(new BigDecimal(picked).intValue()), brightness);
                lightHandler.setLight(false);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    @NotNull
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

            }

            @Override
            public void onError(String msg)
            {
                //do nothing
            }
        };
    }

    private void storeLight()
    {
        LightOperations operations = new LightOperations(getApplicationContext());
        Map<String, Object> content = new HashMap<>();
        content.put("color", picked);
        content.put("brightness", (long) brightness);
        operations.createLight(content);
    }
}
