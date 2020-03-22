package com.wintermute.gmassistant.view.light;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import com.flask.colorpicker.ColorPickerView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.operations.LightOperations;
import com.wintermute.gmassistant.services.LightConnection;
import com.wintermute.gmassistant.view.model.Light;

import java.math.BigDecimal;
import java.util.List;

public class LightConfiguration extends AppCompatActivity
{

    private int brightness;
    private LightOperations light;
    private List<String> lightManagementUrls;
    private Switch lightInRealtime;
    private Switch resetLightsAfterEffect;
    private int sceneLightColor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_config);

        init();
    }

    private void init()
    {
        light = new LightOperations(getApplicationContext());
        //TODO: nullpointer if connected bulbs not present
        lightManagementUrls = LightConnection.getInstance().getBulbs();
        lightInRealtime = findViewById(R.id.light_in_real_time);
        resetLightsAfterEffect = findViewById(R.id.reset_after_effect_finished);

        boolean configForEffect = getIntent().getBooleanExtra("effectBoard", false);
        if (!configForEffect){
            resetLightsAfterEffect.setVisibility(View.GONE);
        }

        initColorPicker();
        initBrightnessBar();
        initSubmitButton();
    }

    private void initColorPicker()
    {
        ColorPickerView colorPickerView = findViewById(R.id.color_picker_view);
        colorPickerView.addOnColorChangedListener(color ->
        {
            sceneLightColor = color;
            double[] picked = light.getRGBtoXY(Color.valueOf(new BigDecimal(color).intValue()));
            if (lightInRealtime.isChecked())
            {
                lightManagementUrls.forEach(url -> light.changeColor(url, picked));
            }
        });
    }

    private void initBrightnessBar()
    {
        SeekBar seekbar = findViewById(R.id.brightness_bar);
        brightness = seekbar.getMax();
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                brightness = progress;
                if (lightInRealtime.isChecked())
                {
                    lightManagementUrls.forEach(url -> light.changeBrightness(url, brightness));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    private void initSubmitButton()
    {
        Button btn = findViewById(R.id.light_submit);
        btn.setOnClickListener(v ->
        {
            Intent result = new Intent();
            try
            {
                Long resetLights = resetLightsAfterEffect.isSelected() ? 1L : 0L;
                Light light = new Light(null, (long) sceneLightColor, (long) brightness, resetLights);
                result.putExtra("light", light);
                setResult(RESULT_OK, result);
            } catch (Exception e)
            {
                e.printStackTrace();
                setResult(RESULT_CANCELED);
            }
            finish();
        });
    }
}
