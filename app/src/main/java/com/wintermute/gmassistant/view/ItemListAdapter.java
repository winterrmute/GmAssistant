package com.wintermute.gmassistant.view;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.model.FileElement;
import com.wintermute.gmassistant.view.ItemList.OnListFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a DummyItem and makes a call to the specified {@link
 * OnListFragmentInteractionListener}. TODO: Replace the implementation with code for your data type.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder>
{
    private static final String UPDATE_DATA_FLAG = "updateData";
    private static final String CLEAR_DATA_FLAG = "clearData";
    private List<FileElement> files;
    private final OnListFragmentInteractionListener mListener;

    public ItemListAdapter(List<FileElement> items, OnListFragmentInteractionListener listener)
    {
        files = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {
        holder.file = files.get(position);
        holder.mContentView.setText(files.get(position).getName());

        holder.mView.setOnClickListener(v ->
        {
            if (null != mListener)
            {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(holder.file);
            }
        });
    }

    public void updateData(List<FileElement> filesList, String flag) {
        if (UPDATE_DATA_FLAG.equals(flag)) { //append
            for (int i = 0; i < filesList.size(); i++) {
                files.add(filesList.get(i));
                notifyItemInserted(getItemCount());
            }
        } else if (CLEAR_DATA_FLAG.equals(flag)) { //clear all
            files.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount()
    {
        return files.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private final View mView;
        private final TextView mContentView;
        private FileElement file;

        private ViewHolder(View view)
        {
            super(view);
            mView = view;
            mContentView = view.findViewById(R.id.content);
        }
    }
}
