package com.wintermute.soundboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.soundboard.R;

import java.util.ArrayList;

/**
 * Adapter for displaying audio files inner playlists.
 *
 * @author wintermute
 */
public class AudioFileAdapter extends BaseAdapter
{

    private ArrayList<String> tracks;
    private LayoutInflater inflater;

    /**
     * Creates an instance.
     *  @param ctx application context.
     * @param tracks displayed by a playlist.
     */
    public AudioFileAdapter(Context ctx, ArrayList<String> tracks)
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
//        LinearLayout result = (LinearLayout) inflater.inflate(R.layout.song, parent, false);
//        TextView songView = result.findViewById(R.id.title);
//        TextView artistView = result.findViewById(R.id.artist);
//        Track currentSong = tracks.get(position);
//        songView.setText(currentSong.getName());
//        artistView.setText(currentSong.getArtist());
//        result.setTag(position);
//        return result;
        //TODO: must take ArrayList<Track> instead of ArrayList<String>
        LinearLayout result = (LinearLayout) inflater.inflate(R.layout.file, parent, false);
        TextView fileName = result.findViewById(R.id.file_name);
        String userPlaylist = tracks.get(position);
        fileName.setText(userPlaylist);
        result.setTag(position);
        return result;
    }
}
