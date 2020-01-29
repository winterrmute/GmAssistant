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

import java.io.File;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>
{

    private List<List<FileElement>> filesListsByCategory;
    private LayoutInflater mInflater;
    private Context ctx;
    private ItemListAdapter adapter;
    private List<FileElement> categoryFiles;

    public ViewPagerAdapter(Context context, List<List<FileElement>> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.filesListsByCategory = data;
        this.ctx = context;
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
        categoryFiles = filesListsByCategory.get(position);

        adapter = new ItemListAdapter(categoryFiles, item ->
        {
            String parent = new File(categoryFiles.get(1).getPath()).getParent();
            if ("previos directory".equals(item.getName())) {
                categoryFiles = fbs.getFiles(parent);
            }
            categoryFiles = fbs.getFiles(item.getPath());
            categoryFiles.add(0, new FileElement("previos directory", ""));
            updateListElements();
        });
        holder.myView.setAdapter(adapter);
    }

    private void updateListElements()
    {
        adapter.updateData(null, "clearData");
        adapter.updateData(categoryFiles, "updateData");
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

