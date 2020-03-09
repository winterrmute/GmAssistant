package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.hue.model.HueBulb;

import java.io.File;
import java.util.List;

/**
 * Adapter for displaying audio files inner playlists.
 *
 * @author wintermute
 */
public class HueBulbAdapter extends BaseAdapter
{

    private List<HueBulb> bulbs;
    private LayoutInflater inflater;

    /**
     * Creates an instance.
     *
     * @param ctx application context.
     * @param bulbs displayed bulbs registered by hue.
     */
    public HueBulbAdapter(Context ctx, List<HueBulb> bulbs)
    {
        this.bulbs = bulbs;
        inflater = LayoutInflater.from(ctx);
    }

    public void updateDisplayedElements(View view)
    {
        view.setBackgroundColor(Color.BLUE);
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return bulbs.size();
    }

    @Override
    public Object getItem(int position)
    {
        return bulbs.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout result = (LinearLayout) inflater.inflate(R.layout.hue_bulb, parent, false);
        TextView bulbName = result.findViewById(R.id.name);
        TextView bulbType = result.findViewById(R.id.type);
        HueBulb target = bulbs.get(position);
        bulbName.setText(target.getName());
        bulbType.setText(target.getType());
        result.setTag(position);
        return result;
    }
}
