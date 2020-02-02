package com.wintermute.gmassistant.view;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.ListAdapter;
import com.wintermute.gmassistant.model.FileElement;

import java.util.ArrayList;
import java.util.List;

public class CategoryList extends Fragment
{
    private int fragmentNumber;
//    private List<FileElement> listElements;
    private List<String> listElements;

    public static CategoryList init(int position, ArrayList<String> elements)
    {
        CategoryList result = new CategoryList();
        Bundle args = new Bundle();
        args.putInt("val", position);
//        args.putParcelableArrayList("elements", elements);
        args.putStringArrayList("elements", elements);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        fragmentNumber = getArguments() != null ? getArguments().getInt("val") : 1;
        listElements = getArguments().getStringArrayList("elements");
//        listElements = listElements != null ? getArguments().getParcelable("elements") : new ArrayList<>();
        System.out.println();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layoutView = inflater.inflate(R.layout.content_list, container, false);
        ListView lv = layoutView.findViewById(R.id.content_items);
        lv.setAdapter(new ListAdapter(getContext(), listElements));
        lv.setOnItemClickListener((parent, view, position, id) ->
        {
            //do stuff
        });
        lv.setOnItemLongClickListener((parent, view, position, id) ->
        {
            //do other stuff
            return false;
        });
        return layoutView;
    }
}
