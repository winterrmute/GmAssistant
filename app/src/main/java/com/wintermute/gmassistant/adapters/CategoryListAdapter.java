package com.wintermute.gmassistant.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.wintermute.gmassistant.view.library.CategoryList;

/**
 * Receives audio library for given category and displays it.
 *
 * @author wintermute
 */
public class CategoryListAdapter extends FragmentStatePagerAdapter
{
    private int tabsCount;
    private boolean selectTrack;

    public CategoryListAdapter(@NonNull FragmentManager fm, int behavior, int tabsCount, boolean selectTrack)
    {
        super(fm, behavior);
        this.tabsCount = tabsCount;
        this.selectTrack = selectTrack;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return CategoryList.init(position, selectTrack);
    }

    @Override
    public int getCount()
    {
        return tabsCount;
    }
}
