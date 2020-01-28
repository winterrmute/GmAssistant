package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.services.FileBrowserService;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>
{

    private List<List<String>> filesListsByCategory;
    private LayoutInflater mInflater;
    private Context ctx;

    public ViewPagerAdapter(Context context, List<List<String>> data)
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
        List<String> categoryFiles = filesListsByCategory.get(position);
        ListAdapter adapter = new ListAdapter(ctx, categoryFiles);

        holder.myView.setAdapter(adapter);
        holder.myView.setOnItemClickListener((parent, view, pos, id) ->
        {
            List<String> files = fbs.getFiles(categoryFiles.get(pos));
            files.add(0, "previous directory");
            ListAdapter tmpAdapter = new ListAdapter(ctx, files);
            holder.myView.setAdapter(tmpAdapter);
        });
    }

    @Override
    public int getItemCount()
    {
        return filesListsByCategory.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        ListView myView;
        RelativeLayout relativeLayout;

        ViewHolder(View itemView)
        {
            super(itemView);

            myView = itemView.findViewById(R.id.track_list);
            relativeLayout = itemView.findViewById(R.id.container);
        }
    }
}

