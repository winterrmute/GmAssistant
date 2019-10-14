package com.wintermute.soundboard.configurator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorListener;
import com.wintermute.soundboard.R;

public class LightConfiguration extends AppCompatActivity
{

    private int picked;

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
            setResult(2, new Intent().putExtra("color", picked));
            finish();
        });

        ColorPickerView colorPickerView = findViewById(R.id.color_picker);

        colorPickerView.setColorListener((ColorListener) (color, fromUser) -> setPicked(color));
    }
}
