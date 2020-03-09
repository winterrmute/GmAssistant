package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.model.Board;

import java.util.List;

/**
 * Adapter for displaying audio files inner playlists.
 *
 * @author wintermute
 */
public class EffectGroupsAdapter extends BaseAdapter
{

    private List<Board> effectGroups;
    private LayoutInflater inflater;

    /**
     * Creates an instance.
     *
     * @param ctx application context.
     * @param effectGroups effects container for board.
     */
    public EffectGroupsAdapter(Context ctx, List<Board> effectGroups)
    {
        this.effectGroups = effectGroups;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount()
    {
        return effectGroups.size();
    }

    @Override
    public Object getItem(int position)
    {
        return effectGroups.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout result = (LinearLayout) inflater.inflate(R.layout.audio_file, parent, false);
        TextView groupName = result.findViewById(R.id.name);
        groupName.setText(effectGroups.get(position).getName());
        result.setTag(position);
        return result;
    }
}
