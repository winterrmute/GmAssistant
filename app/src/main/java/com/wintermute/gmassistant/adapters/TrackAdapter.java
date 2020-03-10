package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.view.model.Track;

import java.util.List;

/**
 * Adapter for displaying audio files inner playlists.
 *
 * @author wintermute
 */
public class TrackAdapter extends BaseAdapter
{

    private List<Track> tracks;
    private LayoutInflater inflater;

    /**
     * Creates an instance.
     *
     * @param ctx application context.
     * @param tracks displayed by a playlist.
     */
    public TrackAdapter(Context ctx, List<Track> tracks)
    {
        this.tracks = tracks;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount()
    {
        return tracks.size();
    }

    @Override
    public Object getItem(int position)
    {
        return tracks.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return Long.parseLong(String.valueOf(tracks.get(position).getId()));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout result = (LinearLayout) inflater.inflate(R.layout.audio_file, parent, false);
        TextView trackView = result.findViewById(R.id.name);
        TextView artistView = result.findViewById(R.id.tag);
        Track target = tracks.get(position);
        trackView.setText(target.getName());
        artistView.setText(target.getTag());
        result.setTag(position);
        return result;
    }
}
