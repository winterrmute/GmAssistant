package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.model.FileElement;
import com.wintermute.gmassistant.services.FileBrowserService;
import com.wintermute.gmassistant.view.ItemListAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>
{

    public static final String PREVIOUS_DIRECTORY = "previous directory";
    private List<List<FileElement>> filesListsByCategory;
    private LayoutInflater mInflater;
    private Context ctx;
    private ItemListAdapter adapter;
    private String rootPath;
    private List<FileElement> rootElements;

    public ViewPagerAdapter(Context context, List<List<FileElement>> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.filesListsByCategory = data;
        this.ctx = context;
        rootElements = new ArrayList<>(filesListsByCategory.get(0));
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(mInflater.inflate(R.layout.item_viewpager, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        FileBrowserService fbs = new FileBrowserService();
        List<FileElement> categoryFiles = new ArrayList<>(filesListsByCategory.get(position));

        adapter = new ItemListAdapter(categoryFiles, item ->
        {
            browseDirectory(fbs, item);
        });
        holder.myView.setAdapter(adapter);
    }

    private void browseDirectory(FileBrowserService fbs, FileElement item)
    {
        Map<String, List<FileElement>> files = fbs.getFiles(item);
        List<FileElement> newContent = files.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        String parent;
        String path = item.getPath();


        if (item.isRoot())
        {
            rootPath = item.getPath();
        }
        FileElement goToParent = new FileElement(PREVIOUS_DIRECTORY, item.getPath(), false);

        if (PREVIOUS_DIRECTORY.equals(item.getName())) {
            if (item.getPath().equals(rootPath)) {
                newContent = rootElements;
                updateListElements(newContent);
            } else {
                goToParent = new FileElement(PREVIOUS_DIRECTORY, item.getPath().substring(item.getPath().lastIndexOf(0, '/')) , false);
                newContent.add(0, goToParent);
                updateListElements(newContent);
            }
        } else {
            newContent.add(0, goToParent);
            updateListElements(newContent);
        }

    }

    private void handlePath(){
        
    }

    private void updateListElements(List<FileElement> newContent)
    {
        adapter.updateData(null, "clearData");
        adapter.updateData(newContent, "updateData");
    }

    @Override
    public int getItemCount()
    {
        return filesListsByCategory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        RecyclerView myView;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView)
        {
            super(itemView);

            myView = itemView.findViewById(R.id.my_list);
            myView.setLayoutManager(new LinearLayoutManager(ctx));
            relativeLayout = itemView.findViewById(R.id.container);
        }
    }
}

