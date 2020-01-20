package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Track;

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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.item_viewpager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        List<Track> currentlyViewed = audioFilesSortedByTags.get(position);
        TrackAdapter adapter = new TrackAdapter(ctx, currentlyViewed);
        holder.myView.setAdapter(adapter);
    }

    @Override
    public int getItemCount()
    {
        return audioFilesSortedByTags.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder
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

