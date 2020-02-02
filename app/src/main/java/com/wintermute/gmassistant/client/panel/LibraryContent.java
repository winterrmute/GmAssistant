package com.wintermute.gmassistant.client.panel;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.CategoryListAdapter;
import com.wintermute.gmassistant.database.dao.DirectoryDao;
import com.wintermute.gmassistant.model.FileElement;
import com.wintermute.gmassistant.view.CategoryList;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibraryContent extends FragmentActivity
{
    private static final int TABS_COUNT = 3;
    CategoryListAdapter adapter;
    ViewPager viewPager;
    TabLayout tabLayout;
//    private static List<FileElement> categoryContent = new ArrayList<>();
    private static List<String> categoryContent = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_content);

        Map<String, List<FileElement>> directoriesWithContent = listByTag();

//        categoryContent = directoriesWithContent.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        categoryContent.add("eins");
        categoryContent.add("zwei");


        adapter = new CategoryListAdapter(getSupportFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, TABS_COUNT, categoryContent);
        viewPager = findViewById(R.id.category_pages);
        viewPager.setAdapter(adapter);
        tabLayout = findViewById(R.id.category_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("music");
        tabLayout.getTabAt(1).setText("ambience");
        tabLayout.getTabAt(2).setText("effect");
    }

    private Map<String, List<FileElement>> listByTag()
    {
        Map<String, List<FileElement>> result = new HashMap<>();
        DirectoryDao dao = new DirectoryDao(getApplicationContext());
        String[] categories = {"music", "ambience", "effect"};

        for (String category : categories)
        {
            List<FileElement> directories =
                dao.getDirectoriesForCategory(category).stream().map(f -> new FileElement( new File(f.getPath()).getName(), f.getPath(), true)).collect(
                    Collectors.toList());
            result.put(category, directories);
        }
        return result;
    }

//    public static class CategoryListAdapter extends FragmentStatePagerAdapter
//    {
//
//        public CategoryListAdapter(@NonNull FragmentManager fm, int behavior)
//        {
//            super(fm, behavior);
//        }
//
//        @NonNull
//        @Override
//        public Fragment getItem(int position)
//        {
//            return CategoryList.init(position, new ArrayList<>(categoryContent));
//        }
//
//        @Override
//        public int getCount()
//        {
//            return TABS_COUNT;
//        }
//    }
}
