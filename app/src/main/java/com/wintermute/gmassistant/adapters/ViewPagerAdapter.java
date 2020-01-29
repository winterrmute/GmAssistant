package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.services.FileBrowserService;
import com.wintermute.gmassistant.view.ItemList;
import com.wintermute.gmassistant.view.ItemListAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>
{

    private List<List<String>> filesListsByCategory;
    private LayoutInflater mInflater;
    private Context ctx;
    private ItemListAdapter adapter;

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
        adapter = new ItemListAdapter(categoryFiles, new ItemList.OnListFragmentInteractionListener()
        {
            @Override
            public void onListFragmentInteraction(String item)
            {
                List<String> files = fbs.getFiles(categoryFiles.get(position));
                files.add(0, "previous directory");
                updateUi(adapter, holder);
            }
        });

        holder.myView.setAdapter(adapter);
    }

    private void updateUi(ItemListAdapter adapter, ViewHolder holder)
    {
        adapter.notifyDataSetChanged();
        holder.myView.setAdapter(adapter);
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

