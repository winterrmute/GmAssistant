package com.wintermute.soundboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.services.database.entities.AudioFile;

import java.util.ArrayList;

/**
 * Adapter for displaying audio files inner playlists.
 *
 * @author wintermute
 */
public class AudioFileAdapter extends BaseAdapter
{

    private ArrayList<AudioFile> tracks;
    private LayoutInflater songInf;

    /**
     * Creates an instance.
     *
     * @param ctx application context.
     * @param tracks displayed by a playlist.
     */
    public AudioFileAdapter(Context ctx, ArrayList<AudioFile> tracks)
    {
        tracks = tracks;
        songInf = LayoutInflater.from(ctx);
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
        LinearLayout result = (LinearLayout) songInf.inflate(R.layout.song, parent, false);
        TextView songView = result.findViewById(R.id.title);
        TextView artistView = result.findViewById(R.id.artist);
        AudioFile currentSong = tracks.get(position);
        songView.setText(currentSong.getTitle());
        artistView.setText(currentSong.getArtist());
        result.setTag(position);
        return result;
    }
}
