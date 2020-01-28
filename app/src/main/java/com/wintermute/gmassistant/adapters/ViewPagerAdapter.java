package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.view.ItemList;
import com.wintermute.gmassistant.view.ItemListAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>
{

    private List<List<String>> audioFilesSortedByTags;
    private LayoutInflater mInflater;
    private Context ctx;

    public ViewPagerAdapter(Context context, List<List<String>> data)
    {
        this.mInflater = LayoutInflater.from(context);
        this.audioFilesSortedByTags = data;
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
        ItemListAdapter adapter = new ItemListAdapter(audioFilesSortedByTags.get(position),
        new ItemList.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(String item)
            {

            }
        });
        holder.myView.setAdapter(adapter);
    }

    @Override
    public int getItemCount()
    {
        return audioFilesSortedByTags.size();
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

