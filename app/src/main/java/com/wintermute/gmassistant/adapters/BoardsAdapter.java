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

    private List<Board> boards;
    private LayoutInflater inflater;

    /**
     * Creates an instance.
     *
     * @param ctx application context.
     * @param effectGroups effects container for board.
     */
    public BoardsAdapter(Context ctx, List<Board> effectGroups)
    {
        this.boards = effectGroups;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount()
    {
        return boards.size();
    }

    @Override
    public Object getItem(int position)
    {
        return boards.get(position);
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
        boardName.setText(boards.get(position).getName());
        result.setTag(position);
        return result;
    }

    public void updateDisplayedElements(List<Board> boards)
    {
        this.boards = boards;
        notifyDataSetChanged();
    }
}
