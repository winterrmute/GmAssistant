package com.wintermute.gmassistant.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.wintermute.gmassistant.view.CategoryList;

/**
 * Receives audio library for given category and displays it.
 *
 * @author wintermute
 */
public class CategoryListAdapter extends FragmentStatePagerAdapter
{
    private int tabsCount;
    private boolean singleTrackSelection;

    public CategoryListAdapter(@NonNull FragmentManager fm, int behavior, int tabsCount, boolean singleTrackSelection)
    {
        super(fm, behavior);
        this.tabsCount = tabsCount;
        this.singleTrackSelection = singleTrackSelection;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return CategoryList.init(position, singleTrackSelection);
    }

    @Override
    public int getCount()
    {
        return tabsCount;
    }
}
