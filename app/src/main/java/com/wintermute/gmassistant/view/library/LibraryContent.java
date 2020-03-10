package com.wintermute.gmassistant.view.library;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.CategoryListAdapter;
import com.wintermute.gmassistant.database.dao.DirectoryDao;
import com.wintermute.gmassistant.database.model.Tags;
import com.wintermute.gmassistant.view.StorageBrowser;
import com.wintermute.gmassistant.view.model.Directory;
import com.wintermute.gmassistant.view.model.LibraryFile;
import com.wintermute.gmassistant.view.model.AudioLibrary;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Allows to manage audio library by categories.
 *
 * @author wintermute
 */
public class LibraryContent extends FragmentActivity
{
    private static final int TABS_COUNT = 3;
    public static final int ADD_LIBRARY = 1;
    private TabLayout tabLayout;
    private String[] categories = {Tags.MUSIC.name(), Tags.AMBIENCE.name(), Tags.EFFECT.name()};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_content);
        updateViewData();

        if (null != getIntent().getExtras())
        {
            tabLayout.selectTab(tabLayout.getTabAt(Tags.valueOf(getIntent().getExtras().getString("tag")).ordinal()));
        }

        Button btn = findViewById(R.id.add_files_with_tag);
        btn.setOnClickListener(l ->
        {
            Intent browser = new Intent(LibraryContent.this, StorageBrowser.class);
            browser.putExtra("createLibrary", true);
            startActivityForResult(browser, ADD_LIBRARY);
        });
    }

    private void updateViewData()
    {
        loadAudioLibrary();
        CategoryListAdapter adapter =
            new CategoryListAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, TABS_COUNT,
                getIntent().getBooleanExtra("selectTrack", false));

        ViewPager viewPager = findViewById(R.id.category_pages);
        viewPager.setOffscreenPageLimit(TABS_COUNT);
        viewPager.setAdapter(adapter);

        tabLayout = findViewById(R.id.category_tabs);
        tabLayout.setupWithViewPager(viewPager);
        Arrays
            .stream(categories)
            .forEach(c -> Objects.requireNonNull(tabLayout.getTabAt(Arrays.asList(categories).indexOf(c))).setText(c));
    }

    private void loadAudioLibrary()
    {
        Map<Integer, List<LibraryFile>> result = new HashMap<>();
        DirectoryDao dao = new DirectoryDao(getApplicationContext());
        AudioLibrary sharedModel = new ViewModelProvider(this).get(AudioLibrary.class);

        for (String category : categories)
        {
            List<LibraryFile> directories = dao
                .getDirectoriesForCategory(category)
                .stream()
                .map(f -> new LibraryFile(new File(f.getPath()).getName(), f.getPath(), true))
                .collect(Collectors.toList());
            result.put(Tags.valueOf(category).ordinal(), directories);
        }
        sharedModel.setAudioLibrary(result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        int fileBrowserReqCode = 1;
        if (resultCode == RESULT_OK)
        {
            if (requestCode == fileBrowserReqCode)
            {
                String path = data.getStringExtra("path");
                storeDirectory(path, data.getBooleanExtra("recursive", true), categories[tabLayout.getSelectedTabPosition()]);
                updateViewData();
            }
            if (resultCode == Tags.MUSIC.ordinal() || resultCode == Tags.AMBIENCE.ordinal() || resultCode == Tags.EFFECT
                .ordinal())
            {
                String path = data.getStringExtra("path");
                Intent intent = new Intent().putExtra("path", path);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
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
