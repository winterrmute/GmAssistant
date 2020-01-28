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
import com.wintermute.gmassistant.database.dto.Directory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Manages audio files by tags.
 *
 * @author wintermute
 */
public class AudioFilePanel extends AppCompatActivity
{
    public static final int BROWSE_FILES = 1;
    private String currentTab;
    private ViewPagerAdapter adapter;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Map<String, List<String>> content;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_file_panel);

        viewPager = findViewById(R.id.view_pager2);
        tabLayout = findViewById(R.id.tabs);
        Button addFilesByTag = findViewById(R.id.add_files_with_tag);

        if (null == currentTab)
        {
            currentTab = "music";
        }

        content = listByTag();

        adapter = new ViewPagerAdapter(getApplicationContext(), new ArrayList<>(content.values()));
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager,
            (tab, position) -> tab.setText(content.keySet().toArray()[position].toString())).attach();

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

    private void updateViweData()
    {
        content = listByTag();
        adapter = new ViewPagerAdapter(getApplicationContext(), new ArrayList<>(content.values()));
        viewPager.setAdapter(adapter);
    }

    private Map<String, List<String>> listByTag()
    {
        Map<String, List<String>> result = new HashMap<>();
        DirectoryDao dao = new DirectoryDao(getApplicationContext());
        String[] categories = {"music", "ambience", "effect"};

        for (String category : categories)
        {
            List<String> directories =
                dao.getDirectoriesForCategory(category).stream().map(Directory::getPath).collect(Collectors.toList());

            result.put(category, directories);
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == BROWSE_FILES)
        {
            path = data.getStringExtra("path");
            storeDirectory(path, data.getBooleanExtra("includeSubdirs", true), currentTab);
            updateViweData();
        }
    }

    private void storeDirectory(String path, boolean recursive, String tag)
    {
        Directory directory = new Directory();
        directory.setPath(path);
        directory.setTag(tag);
        directory.setRecursive(recursive);
        DirectoryDao dao = new DirectoryDao(getApplicationContext());
        directory.setId(dao.insert(directory));
    }
}
