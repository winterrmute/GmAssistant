package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.hue.model.HueBridge;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for paired hue bridges.
 *
 * @author wintermute
 */
public class HueBridgeAdapter extends BaseAdapter
{
    private List<HueBridge> bridges;
    private LayoutInflater inflater;

    /**
     * Creates an instance.
     *
     * @param ctx application context
     * @param bridges to display within the list view.
     */
    public HueBridgeAdapter(Context ctx, List<HueBridge> bridges)
    {
        this.bridges = bridges;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount()
    {
        return bridges.size();
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
        HueBridge bridge = bridges.get(position);
        fileName.setText(bridge.getName());
        result.setTag(position);
        return result;
    }

    public void update(List<HueBridge> newContent)
    {
        bridges = new ArrayList<>(newContent);
        notifyDataSetChanged();
    }
}
