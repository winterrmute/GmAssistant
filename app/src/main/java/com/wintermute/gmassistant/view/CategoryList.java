package com.wintermute.gmassistant.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.ListAdapter;
import com.wintermute.gmassistant.handlers.PlayerHandler;
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
    private static final String PREVIOUS_DIRECTORY = "previous directory";
    private String rootPath;
    private List<LibraryElement> rootElements;
    private List<LibraryElement> listElements;
    private int tagId;
    private ListAdapter adapter;

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
        tagId = getArguments() != null ? getArguments().getInt("tag") : 0;
        listElements = getArguments() != null ? getArguments().getParcelableArrayList("elements") : new ArrayList<>();
        System.out.println();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layoutView = inflater.inflate(R.layout.content_list, container, false);
        ListView lv = layoutView.findViewById(R.id.content_items);
        adapter = new ListAdapter(getContext(), listElements);
        lv.setAdapter(adapter);
        rootElements = listElements;
        FileBrowserService fbs = new FileBrowserService();

        lv.setOnItemClickListener((parent, view, position, id) ->
        {
            handleClickOnElement(fbs, listElements.get(position));
        });
        lv.setOnItemLongClickListener((parent, view, position, id) ->
        {
            //do other stuff
            return false;
        });
        return layoutView;
    }

    private void handleClickOnElement(FileBrowserService fbs, LibraryElement item)
    {
        if (new File(item.getPath()).isDirectory())
        {
            List<LibraryElement> newContent;
            String newPath;
            if (item.isRoot())
            {
                rootPath = item.getPath();
            }

            if (PREVIOUS_DIRECTORY.equals(item.getName()))
            {
                newPath = item.getPath().substring(0, item.getPath().lastIndexOf('/'));
            } else
            {
                newPath = item.getPath();
            }
            LibraryElement goToParent = new LibraryElement(PREVIOUS_DIRECTORY, newPath, false);

            if (item.getPath().equals(rootPath) && !item.isRoot())
            {
                newContent = rootElements;
            } else
            {
                newContent = fbs.getFiles(newPath);
                newContent.add(0, goToParent);
            }
            listElements = new ArrayList<>(newContent);
            adapter.updateDisplayedElements(listElements);
        } else
        {
            PlayerHandler handler = new PlayerHandler(getContext());
            handler.playSingleFile(item.getPath(), tagId);
        }
    }
}
