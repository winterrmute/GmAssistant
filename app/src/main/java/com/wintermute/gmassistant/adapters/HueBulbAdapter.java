package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.hue.model.HueBulb;

import java.util.List;

/**
 * Adapter for paired hue bridges.
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
     * @param ctx application context
     * @param bulbs to display within the list view.
     */
    public HueBulbAdapter(Context ctx, List<HueBulb> bulbs)
    {
        this.bulbs = bulbs;
        inflater = LayoutInflater.from(ctx);
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
        ConstraintLayout result = (ConstraintLayout) inflater.inflate(R.layout.hue_bulb, parent, false);
        TextView name = result.findViewById(R.id.name);
        TextView type = result.findViewById(R.id.type);
        HueBulb bulb = bulbs.get(position);
        name.setText(bulb.getName());
        type.setText(bulb.getType());
        result.setTag(position);
        return result;
    }
}
