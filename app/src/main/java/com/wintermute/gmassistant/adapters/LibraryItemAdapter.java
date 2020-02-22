package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.model.LibraryFile;

import java.util.List;

/**
 * Creates view of browsed files.
 *
 * @author wintermute
 */
public class LibraryItemAdapter extends BaseAdapter
{
    private List<LibraryFile> browsedFiles;
    private LayoutInflater inflater;

    public LibraryItemAdapter(Context ctx, List<LibraryFile> files)
    {
        browsedFiles = files;
        inflater = LayoutInflater.from(ctx);
    }

    public void updateDisplayedElements(List<LibraryFile> files)
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
        return browsedFiles.get(position);
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
        String file = browsedFiles.get(position).getName();
        fileName.setText(file);
        result.setTag(position);
        return result;
    }
}
