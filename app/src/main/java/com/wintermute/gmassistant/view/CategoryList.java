package com.wintermute.gmassistant.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.LibraryItemAdapter;
import com.wintermute.gmassistant.handlers.PlayerHandler;
import com.wintermute.gmassistant.helper.Categories;
import com.wintermute.gmassistant.model.LibraryElement;
import com.wintermute.gmassistant.services.FileBrowserService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents each audio category elements as list. Allows to browse it.
 *
 * @author wintermute
 */
public class CategoryList extends Fragment
{
    private List<LibraryElement> rootElements;
    private List<LibraryElement> listElements;
    private int tagId;
    private LibraryItemAdapter adapter;

    public static CategoryList init(int position, ArrayList<LibraryElement> elements)
    {
        CategoryList result = new CategoryList();
        Bundle args = new Bundle();
        args.putInt("tag", position);
        args.putParcelableArrayList("elements", elements);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        tagId = getArguments() != null ? getArguments().getInt("tag") : Categories.MUSIC.ordinal();
        listElements = getArguments() != null ? getArguments().getParcelableArrayList("elements") : new ArrayList<>();
        System.out.println();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layoutView = inflater.inflate(R.layout.content_list, container, false);
        ListView lv = layoutView.findViewById(R.id.content_items);
        adapter = new LibraryItemAdapter(getContext(), listElements);
        lv.setAdapter(adapter);
        rootElements = listElements;
        FileBrowserService fbs = new FileBrowserService();

        lv.setOnItemClickListener((parent, view, position, id) ->
        {
            List<LibraryElement> libraryElements = fbs.browseLibrary(listElements.get(position), rootElements);

            if (libraryElements.size() == 1 && !new File(libraryElements.get(0).getPath()).isDirectory())
            {
                PlayerHandler handler = new PlayerHandler(getContext());
                handler.playSingleFile(libraryElements.get(0).getPath(), tagId);
            } else
            {
                listElements = libraryElements;
                adapter.updateDisplayedElements(listElements);
            }
        });
        lv.setOnItemLongClickListener((parent, view, position, id) ->
        {
            //do other stuff
            return false;
        });
        return layoutView;
    }
}
