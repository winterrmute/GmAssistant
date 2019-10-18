package com.wintermute.gmassistant.dialogs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.wintermute.gmassistant.R;

import java.util.ArrayList;

public class ListDialog extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dialog);

        ListView optsView = findViewById(R.id.opts);
        ArrayList<String> opts = getIntent().getStringArrayListExtra("opts");
        ArrayAdapter<String> itemsAdapter =
            new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, opts);
        optsView.setAdapter(itemsAdapter);

        optsView.setOnItemClickListener((parent, view, position, id) ->
        {
            setResult(RESULT_OK, new Intent().putExtra("selected", opts.get(position)));
            finish();
        });
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
