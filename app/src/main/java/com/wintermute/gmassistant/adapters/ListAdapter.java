package com.wintermute.gmassistant.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.model.Directory;
import com.wintermute.gmassistant.model.FileElement;

import java.io.File;
import java.util.List;

/**
 * Creates view of browsed files.
 *
 * @author wintermute
 */
public class ListAdapter extends BaseAdapter
{
    private List<String> browsedFiles;
    private LayoutInflater inflater;

    public ListAdapter(Context ctx, List<String> files)
    {
        browsedFiles = files;
        inflater = LayoutInflater.from(ctx);
    }

    public void updateDisplayedElements(List<String> files)
    {
        browsedFiles = files;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return browsedFiles.size();
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
//        String file = new File(browsedFiles.get(position).getPath()).getName();
        String file = browsedFiles.get(position);
        fileName.setText(file);
        result.setTag(position);
        return result;
    }
}
