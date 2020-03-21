package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.view.model.Board;

import java.util.List;

/**
 * Adapter for displaying audio files inner playlists.
 *
 * @author wintermute
 */
public class BoardsAdapter extends BaseAdapter
{

    private List<Board> adapterList;
    private LayoutInflater inflater;

    /**
     * Creates an instance.
     *
     * @param ctx application context.
     * @param boards effects container for board.
     */
    public BoardsAdapter(Context ctx, List<Board> boards)
    {
        adapterList = boards;
        inflater = LayoutInflater.from(ctx);
    }

    public void updateDisplayedElements(List<Board> boards)
    {
        adapterList = boards;
        notifyDataSetChanged();
    }

    @Override
    public int getCount()
    {
        return adapterList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return adapterList.get(position);
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
        TextView boardName = result.findViewById(R.id.file_name);
        boardName.setText(adapterList.get(position).getName());
        result.setTag(position);
        return result;
    }
}
