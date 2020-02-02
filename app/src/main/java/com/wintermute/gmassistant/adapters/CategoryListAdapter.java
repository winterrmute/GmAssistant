package com.wintermute.gmassistant.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.wintermute.gmassistant.model.FileElement;
import com.wintermute.gmassistant.view.CategoryList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Receives audio library for given category and displays it.
 *
 * @author wintermute
 */
public class CategoryListAdapter extends FragmentStatePagerAdapter
{
    private int tabsCount;
    private Map<Integer, List<FileElement>> audioByCategory;

    public CategoryListAdapter(@NonNull FragmentManager fm, int behavior, int tabsCount, Map<Integer, List<FileElement>> library)
    {
        super(fm, behavior);
        this.tabsCount = tabsCount;
        this.audioByCategory = library;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return CategoryList.init(position, new ArrayList<>(Objects.requireNonNull(audioByCategory.get(position))));
    }

    @Override
    public int getCount()
    {
        return tabsCount;
    }
}
