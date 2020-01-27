package com.wintermute.gmassistant.alert;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.wintermute.gmassistant.R;

public class ResultDialog extends DialogFragment
{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.include_subdirs_message)
            .setPositiveButton(R.string.ok_result, (dialog, id) ->
            {
                // FIRE ZE MISSILES!
            })
            .setNegativeButton(R.string.cancel_result, (dialog, id) ->
            {
                // User cancelled the dialog
            });
        // Create the ResultDialog object and return it
        return builder.create();
    }

}
