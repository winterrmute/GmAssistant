package com.wintermute.gmassistant.client.panel;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.ViewPagerAdapter;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AudioFilePanel extends AppCompatActivity
{

    ViewPager2 viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_file_panel);

        viewPager = findViewById(R.id.view_pager2);
        tabLayout = findViewById(R.id.tabs);

        viewPager.setAdapter(new ViewPagerAdapter(this, new ArrayList<>(listByTag().values())));
        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> tab.setText(listByTag().keySet().toArray()[position].toString())).attach();
    }

    private Map<String, List<Track>> listByTag()
    {
        TrackDao dao = new TrackDao(this);
        return Stream.of("unsorted", "music", "ambience", "effect")
            .collect(Collectors.toMap(k -> k, dao::getTracksByTag));
    }
}
