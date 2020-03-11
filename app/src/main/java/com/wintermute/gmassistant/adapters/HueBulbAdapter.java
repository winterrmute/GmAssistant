package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.hue.model.HueBulb;

import java.util.ArrayList;
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
    private Context ctx;

    /**
     * Creates an instance.
     *
     * @param ctx application context
     * @param bulbs to display within the list view.
     */
    public HueBulbAdapter(Context ctx, List<HueBulb> bulbs)
    {
        this.ctx = ctx;
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
        ConstraintLayout result = (ConstraintLayout) inflater.inflate(R.layout.hue_bulb, parent, false);
        CheckedTextView name = result.findViewById(R.id.name);
        TextView type = result.findViewById(R.id.type);
        HueBulb bulb = bulbs.get(position);
        name.setText(bulb.getName());
        type.setText(bulb.getType());
        result.setTag(position);
        return result;
    }
}
