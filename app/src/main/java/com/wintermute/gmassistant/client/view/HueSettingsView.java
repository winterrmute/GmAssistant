package com.wintermute.gmassistant.client.view;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;

public class HueSettingsView extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_settings);

        Button connectHue = findViewById(R.id.connect_hue);
        connectHue.setOnClickListener(v -> establish());
    }

    private void establish()
    {

    }
}
