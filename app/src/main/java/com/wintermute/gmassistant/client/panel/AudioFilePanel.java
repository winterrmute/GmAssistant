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

/**
 * Manages audio files by tags.
 *
 * @author wintermute
 */
public class AudioFilePanel extends AppCompatActivity
{
    public static final int BROWSE_FILES = 1;
    private String currentTab;

    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_file_panel);

        ViewPager2 viewPager = findViewById(R.id.view_pager2);
        TabLayout tabLayout = findViewById(R.id.tabs);
        Button addFilesByTag = findViewById(R.id.add_files_with_tag);

        if (null == currentTab)
        {
            currentTab = "music";
        }

        Map<String, List<String>> content = listByTag();

        viewPager.setAdapter(new ViewPagerAdapter(getApplicationContext(), new ArrayList<>(content.values())));
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

    private Map<String, List<String>> listByTag()
    {
        Map<String, List<String>> result = new HashMap<>();
        DirectoryDao dirDao = new DirectoryDao(getApplicationContext());
        String[] categories = {"music", "ambience", "effect"};

        for (String category : categories)
        {
            //            List<String> files = dirDao
            //                .getDirectoriesForCategory(category)
            //                .stream()
            //                .map(Directory::getPath)
            //                .collect(Collectors.toList());
            result.put(category, getFiles(dirDao, category));
        }
        return result;
        //        return Stream
        //            .of("music", "ambience", "effect")
        //            .collect(Collectors.toMap(k -> k, dao::getTracksByTag));
    }

    private List<String> getFiles(DirectoryDao dao, String tag)
    {
        List<String> result = new ArrayList<>();
        List<Directory> directoriesForCategory = dao.getDirectoriesForCategory(tag);
        for (int i = 0; i < directoriesForCategory.size(); i++)
        {
            result.add(directoriesForCategory.get(i).getPath());
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
