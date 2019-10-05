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
 * Creates adapter for song view.
 *
 * @author wintermute
 */
public class AudioFileAdapter extends BaseAdapter
{

    private ArrayList<AudioFile> songs;
    private LayoutInflater songInf;

    public AudioFileAdapter(Context ctx, ArrayList<AudioFile> theSongs)
    {
        songs = theSongs;
        songInf = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount()
    {
        return songs.size();
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
        LinearLayout songLayout = (LinearLayout) songInf.inflate(R.layout.song, parent, false);
        TextView songView = songLayout.findViewById(R.id.title);
        TextView artistView = songLayout.findViewById(R.id.artist);
        AudioFile currentSong = songs.get(position);
        songView.setText(currentSong.getTitle());
        artistView.setText(currentSong.getArtist());
        songLayout.setTag(position);
        return songLayout;
    }
}
