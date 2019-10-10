package com.wintermute.soundboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.model.Playlist;
import java.util.List;

/**
 * Adapter for displaying browsed files.
 *
 * @author wintermute
 */
public class PlaylistAdapter extends BaseAdapter
{
    private List<Playlist> userPlaylists;
    private LayoutInflater inflater;

    /**
     * Creates an instance.
     *
     * @param ctx application context
     * @param playlists to display within the list view.
     */
    public PlaylistAdapter(Context ctx, List<Playlist> playlists)
    {
        this.userPlaylists = playlists;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount()
    {
        return userPlaylists.size();
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
        LinearLayout result = (LinearLayout) inflater.inflate(R.layout.file, parent, false);
        TextView fileName = result.findViewById(R.id.file_name);
        Playlist userPlaylist = userPlaylists.get(position);
        fileName.setText(userPlaylist.getName());
        result.setTag(position);
        return result;
    }
}
