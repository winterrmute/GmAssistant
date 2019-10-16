package com.wintermute.gmassistant.configurator;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.SeekBar;
import androidx.appcompat.app.AppCompatActivity;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.handler.LightHandler;

import java.math.BigDecimal;

public class LightConfiguration extends AppCompatActivity
{

    private int picked;
    private int brightness;
    private LightHandler lightHandler;

    public void setPicked(int picked)
    {
        this.picked = picked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_handler);

        Button btn = findViewById(R.id.light_submit);
        btn.setOnClickListener(v ->
        {
            setResult(2, new Intent().putExtra("color", picked).putExtra("brightness", brightness));
            lightHandler.setLight(true);
            finish();
        });

        ColorPickerView colorPickerView = findViewById(R.id.color_picker);
        colorPickerView.setColorListener((ColorListener) (color, fromUser) ->
        {
            setPicked(color);
            lightHandler =
                new LightHandler(getBaseContext(), Color.valueOf(new BigDecimal(color).intValue()), brightness);
            lightHandler.setLight(false);
        });

        SeekBar seekbar = findViewById(R.id.brightness_bar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
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
}
