package com.wintermute.gmassistant.client.panel;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.ViewPagerAdapter;
import com.wintermute.gmassistant.client.FileBrowser;
import com.wintermute.gmassistant.database.dao.DirectoryDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Directory;
import com.wintermute.gmassistant.database.dto.Track;
import com.wintermute.gmassistant.services.FileBrowserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Manages audio files by tags.
 *
 * @author wintermute
 */
public class AudioFilePanel extends AppCompatActivity
{
    public static final int BROWSE_FILES = 1;
    ViewPager2 viewPager;
    TabLayout tabLayout;
    String currentTab;
    Button addFilesByTag;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_file_panel);

        currentTab = "unsorted";

        viewPager = findViewById(R.id.view_pager2);
        tabLayout = findViewById(R.id.tabs);
        addFilesByTag = findViewById(R.id.add_files_with_tag);

        viewPager.setAdapter(new ViewPagerAdapter(this, new ArrayList<>(listByTag().values())));
        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> tab.setText(listByTag().keySet().toArray()[position].toString())).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener()
        {
            @Override
            public void onTabSelected(TabLayout.Tab tab)
            {
                currentTab = tab.getText().toString();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab)
            {

            }
        });
        addFilesByTag.setOnClickListener(l ->
        {
            Intent fileBrowser = new Intent(AudioFilePanel.this, FileBrowser.class);
            startActivityForResult(fileBrowser, 1);
        });
    }

    private Map<String, List<Track>> listByTag()
    {
        TrackDao dao = new TrackDao(this);
        return Stream
            .of("music", "ambience", "effect", "unsorted")
            .collect(Collectors.toMap(k -> k, dao::getTracksByTag));
    }

    private List<Directory> getFilesList(String tag)
    {
        List<Directory> directories =
            new DirectoryDao(getApplicationContext()).getDirectoriesForCategory(tag);

        FileBrowserService fbs = new FileBrowserService();
        fbs.collectTracks(path);

        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == BROWSE_FILES)
        {
            path = data.getStringExtra("path");
            storeDirectory(path, data.getBooleanExtra("includeSubdirs", true), currentTab);
        }
    }

    private void storeDirectory(String path, boolean recursive, String tag)
    {
        if (tag.isEmpty())
        {
            tag = "unsorted";
        }
        DirectoryDao dao = new DirectoryDao(getApplicationContext());
        Directory directory = new Directory();
        directory.setPath(path);
        directory.setTag(tag);
        directory.setRecursive(recursive);
        dao.insert(directory);
    }
}
