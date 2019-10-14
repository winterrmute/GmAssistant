package com.wintermute.soundboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.database.dto.Track;

import java.util.List;

/**
 * Adapter for displaying audio files inner playlists.
 *
 * @author wintermute
 */
public class AudioFileAdapter extends BaseAdapter
{

    private List<Track> tracks;
    private LayoutInflater inflater;

    /**
     * Creates an instance.
     *
     * @param ctx application context.
     * @param tracks displayed by a playlist.
     */
    public AudioFileAdapter(Context ctx, List<Track> tracks)
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
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout result = (LinearLayout) inflater.inflate(R.layout.song, parent, false);
        TextView trackView = result.findViewById(R.id.title);
        TextView artistView = result.findViewById(R.id.tag);
        Track target = tracks.get(position);
        trackView.setText(target.getName());
        artistView.setText(target.getTag());
        result.setTag(position);
        return result;
    }
}
