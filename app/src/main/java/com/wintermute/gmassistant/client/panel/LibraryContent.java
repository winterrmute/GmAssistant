package com.wintermute.gmassistant.client.panel;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.CategoryListAdapter;
import com.wintermute.gmassistant.client.FileBrowser;
import com.wintermute.gmassistant.database.dao.DirectoryDao;
import com.wintermute.gmassistant.helper.Categories;
import com.wintermute.gmassistant.model.Directory;
import com.wintermute.gmassistant.model.LibraryElement;

import java.io.File;
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
    private TabLayout tabLayout;
    private String[] categories = {Categories.MUSIC.name(), Categories.AMBIENCE.name(), Categories.EFFECT.name()};

    private void updateViewData()
    {
        Map<Integer, List<LibraryElement>> libraryContent = listByTag();
        CategoryListAdapter adapter =
            new CategoryListAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, TABS_COUNT,
                libraryContent);
        ViewPager viewPager = findViewById(R.id.category_pages);
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.category_tabs);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(categories[0]);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(categories[1]);
        Objects.requireNonNull(tabLayout.getTabAt(2)).setText(categories[2]);
    }

    private Map<Integer, List<LibraryElement>> listByTag()
    {
        Map<Integer, List<LibraryElement>> result = new HashMap<>();
        DirectoryDao dao = new DirectoryDao(getApplicationContext());

        for (String category : categories)
        {
            List<LibraryElement> directories = dao
                .getDirectoriesForCategory(category)
                .stream()
                .map(f -> new LibraryElement(new File(f.getPath()).getName(), f.getPath(), true))
                .collect(Collectors.toList());

            result.put(Categories.valueOf(category).ordinal(), directories);
        }
        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        int fileBrowserReqCode = 1;
        if (resultCode == RESULT_OK && requestCode == fileBrowserReqCode)
        {
            String path = data.getStringExtra("path");
            storeDirectory(path, data.getBooleanExtra("includeSubdirs", true),
                categories[tabLayout.getSelectedTabPosition()]);
            updateViewData();
        }
        if (resultCode == Categories.MUSIC.ordinal() || resultCode == Categories.AMBIENCE.ordinal()
            || resultCode == Categories.EFFECT.ordinal())
        {
            String path = data.getStringExtra("path");
            Intent intent = new Intent().putExtra("path", path);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_content);

        updateViewData();

        if (null != getIntent().getExtras())
        {
            tabLayout.selectTab(
                tabLayout.getTabAt(Categories.valueOf(getIntent().getExtras().getString("tag")).ordinal()));
        }

        Button btn = findViewById(R.id.add_files_with_tag);
        btn.setOnClickListener(l ->
        {
            Intent fileBrowser = new Intent(LibraryContent.this, FileBrowser.class);
            startActivityForResult(fileBrowser, 1);
        });
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
