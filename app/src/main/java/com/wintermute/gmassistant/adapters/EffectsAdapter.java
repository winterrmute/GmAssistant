package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.model.Track;

import java.util.List;

/**
 * Adapter for displaying audio files inner playlists.
 *
 * @author wintermute
 */
public class EffectsAdapter extends BaseAdapter
{

    private List<Track> effects;
    private LayoutInflater inflater;

    /**
     * Creates an instance.
     *
     * @param ctx application context.
     * @param effect effects contained by requested group.
     */
    public EffectsAdapter(Context ctx, List<Track> effect)
    {
        this.effects = effect;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount()
    {
        return effects.size();
    }

    @Override
    public Object getItem(int position)
    {
        return effects.get(position);
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
        TextView effectName = result.findViewById(R.id.name);
        String name =
            effects.get(position).getName().length() > 20 ? effects.get(position).getName().substring(0, 17) + "..."
                                                          : effects.get(position).getName();
        effectName.setText(name);
        result.setTag(position);
        return result;
    }
}
