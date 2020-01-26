package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.database.dto.Track;
import com.wintermute.gmassistant.handlers.PlayerHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>
{

    private List<List<Track>> audioFilesSortedByTags;
    private LayoutInflater mInflater;
    private Context ctx;

    public ViewPagerAdapter(Context context, List<List<Track>> data)
    {
        this.ctx = context;
        this.mInflater = LayoutInflater.from(context);
        this.audioFilesSortedByTags = data;
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
        List<Track> currentlyViewed = audioFilesSortedByTags.get(position);
        TrackAdapter adapter = new TrackAdapter(ctx, currentlyViewed);
        holder.myView.setAdapter(adapter);

        holder.myView.setOnItemClickListener((parent, view, pos, id) ->
        {
            PlayerHandler handler = new PlayerHandler(ctx);
            handler.startPlaying(currentlyViewed.get(pos));
        });
    }

    @Override
    public int getItemCount()
    {
        return audioFilesSortedByTags.size();
    }

    // stores and recycles views as they are scrolled off screen
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

