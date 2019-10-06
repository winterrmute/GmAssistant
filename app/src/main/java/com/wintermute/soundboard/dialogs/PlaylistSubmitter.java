package com.wintermute.soundboard.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.fragment.app.DialogFragment;
import com.wintermute.soundboard.R;

/**
 * Represents playlist submit dialog.
 *
 * @author wintermute
 */
public class PlaylistSubmitter extends DialogFragment
{

    private static final String TAG = "PlaylistSubmitter";
    private EditText playlistName;
    private Button submit;
    private Button cancel;
    public OnInputListener onInputListener;

    /**
     * Creates an instance.
     */
    public PlaylistSubmitter() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.playlist_submitter, container, false);

        playlistName = view.findViewById(R.id.playlist_name);
        submit = view.findViewById(R.id.submit);
        cancel = view.findViewById(R.id.cancel);

        cancel.setOnClickListener(v ->
        {
            getDialog().dismiss();
        });

        submit.setOnClickListener(v ->
        {
            String result = playlistName.getText().toString();
            onInputListener.sendInput(result);
            getDialog().dismiss();
        });

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        try
        {
            onInputListener = (OnInputListener) getActivity();
        } catch (ClassCastException e)
        {
            Log.e(TAG, "onAttach: " + e.getMessage());
        }
    }

    public interface OnInputListener
    {
        void sendInput(String playlistName);
    }
}
