package com.wintermute.soundboard.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.soundboard.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Creates view of browsed files.
 *
 * @author wintermute
 */
public class FileAdapter extends BaseAdapter
{
    private ArrayList<File> browsedFiles;
    private LayoutInflater inflater;

    public FileAdapter(Context ctx, ArrayList<File> files){
        browsedFiles = files;
        inflater = LayoutInflater.from(ctx);
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
        File file = browsedFiles.get(position);
        fileName.setText(file.getName());
        result.setTag(position);
        return result;
    }
}
