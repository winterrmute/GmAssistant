package com.wintermute.gmassistant.client.panel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.ViewPagerAdapter;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Track;

import java.util.ArrayList;
import java.util.List;

public class AudioFilePanel extends AppCompatActivity
{

    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_file_panel);

        viewPager2 = findViewById(R.id.viewPager2);

        List<List<Track>> audioFilesByTags = prepareData();

        viewPager2.setAdapter(new ViewPagerAdapter(this, audioFilesByTags));
    }

    private List<List<Track>> prepareData()
    {
        List<List<Track>> result = new ArrayList<>();
        TrackDao dao = new TrackDao(this);
        String[] audioFilesByTags = {"music", "ambience", "effect", null};
        for (int i = 0; i < audioFilesByTags.length; i++)
        {
            result.add(dao.getTracksByTag(audioFilesByTags[i]));
        }

        return result;
    }
}
