package com.wintermute.gmassistant.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.LibraryItemAdapter;
import com.wintermute.gmassistant.handlers.PlayerHandler;
import com.wintermute.gmassistant.helper.Tags;
import com.wintermute.gmassistant.model.LibraryFile;
import com.wintermute.gmassistant.services.FileBrowserService;
import com.wintermute.gmassistant.view.model.AudioLibrary;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Represents each audio category elements as list. Allows to browse it.
 *
 * @author wintermute
 */
public class CategoryList extends Fragment
{

    private List<LibraryFile> filesInLibrary;
    private int tagId;
    private boolean selectTrack;
    private LibraryItemAdapter adapter;

    public static CategoryList init(int position, boolean selectTrack)
    {
        CategoryList result = new CategoryList();
        Bundle args = new Bundle();
        args.putInt("tag", position);
        args.putBoolean("selectTrack", selectTrack);
        result.setArguments(args);
        return result;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null)
        {
            tagId = getArguments() != null ? getArguments().getInt("tag") : Tags.MUSIC.ordinal();
            selectTrack = getArguments() != null && getArguments().getBoolean("selectTrack");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layoutView = inflater.inflate(R.layout.content_list, container, false);
        ListView lv = layoutView.findViewById(R.id.content_items);

        AudioLibrary sharedModel = new ViewModelProvider(requireActivity()).get(AudioLibrary.class);
        filesInLibrary = sharedModel.getAudioLibrary().get(tagId);

        adapter = new LibraryItemAdapter(getContext(), filesInLibrary);
        lv.setAdapter(adapter);
        FileBrowserService fbs = new FileBrowserService();

        lv.setOnItemClickListener((parent, view, position, id) ->
        {
            List<LibraryFile> libraryElements = fbs.browseLibrary(
                filesInLibrary.get(position), sharedModel.getAudioLibrary().get(tagId));

            if ((libraryElements.size() == 1) && !new File(libraryElements.get(0).getPath()).isDirectory())
            {
                if (selectTrack) {
                    Intent returnSingleTrack = new Intent();
                    returnSingleTrack.putExtra("path", libraryElements.get(0).getPath());
                    Objects.requireNonNull(getActivity()).setResult(Activity.RESULT_OK, returnSingleTrack);
                    Objects.requireNonNull(getActivity()).finish();
                } else {
                    PlayerHandler handler = new PlayerHandler(getContext());
                    handler.play(libraryElements.get(0).getPath(), tagId);
                }
            } else
            {
                filesInLibrary = libraryElements;
                adapter.updateDisplayedElements(filesInLibrary);
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
