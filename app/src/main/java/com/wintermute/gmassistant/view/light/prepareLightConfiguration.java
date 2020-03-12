package com.wintermute.gmassistant.view.light;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.operations.LightConfigOperations;
import com.wintermute.gmassistant.operations.LightOperations;
import com.wintermute.gmassistant.services.LightConnection;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class prepareLightConfiguration extends AppCompatActivity
{

    private int brightness;
    private LightOperations light;
    private List<String> lightManagementUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_handler);

        LightConfigOperations lightConfig = new LightConfigOperations(getApplicationContext());
        light = new LightOperations(getApplicationContext());
        lightManagementUrls = LightConnection.getInstance().getLightManagementUrls();

        Button btn = findViewById(R.id.light_submit);
        btn.setOnClickListener(v ->
        {
            storeLight();

            setResult(RESULT_OK);
            finish();
        });

        ColorPickerView colorPickerView = findViewById(R.id.color_picker);
        colorPickerView.setColorListener((ColorListener) (color, fromUser) ->
        {
            double[] picked = light.getRGBtoXY(Color.valueOf(new BigDecimal(color).intValue()));
            lightManagementUrls.forEach(url -> light.changeColor(url, picked));
        });

        SeekBar seekbar = findViewById(R.id.brightness_bar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                brightness = progress;
                lightManagementUrls.forEach(url -> light.changeBrightness(url, brightness));
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

    private void storeLight()
    {
        LightOperations operations = new LightOperations(getApplicationContext());
        Map<String, Object> content = new HashMap<>();
        //        content.put("color", picked);
        content.put("brightness", (long) brightness);
        operations.createLight(content);
    }
}
