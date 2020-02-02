package com.wintermute.gmassistant.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.wintermute.gmassistant.model.FileElement;
import com.wintermute.gmassistant.view.CategoryList;

import java.util.ArrayList;
import java.util.List;

public class CategoryListAdapter extends FragmentStatePagerAdapter
{
    private int tabsCount;
//    private List<FileElement> list;
    private List<String> list;

    public CategoryListAdapter(@NonNull FragmentManager fm, int behavior, int tabsCount, List<String> list)
    {
        super(fm, behavior);
        this.tabsCount = tabsCount;
        this.list = list;
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        return CategoryList.init(position, new ArrayList<>(list));
    }

    @Override
    public int getCount()
    {
        return tabsCount;
    }
}
