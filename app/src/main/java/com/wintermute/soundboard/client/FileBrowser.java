package com.wintermute.soundboard.client;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.services.FileBrowserService;

import java.io.File;
import java.util.ArrayList;

public class FileBrowser extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        renderFilesAsList();
    }

    void renderFilesAsList()
    {
        FileBrowserService fileBrowserService = new FileBrowserService();

        ListView dirContent = findViewById(R.id.dir_content);
        ArrayList<File> browsedFiles = fileBrowserService.scanDir("");

        ArrayAdapter adapter =
            new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_2, android.R.id.text1, browsedFiles);
        dirContent.setAdapter(adapter);

        dirContent.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (browsedFiles.get(position).isDirectory())
                {
                    Toast.makeText(FileBrowser.this, "Here be dir content", Toast.LENGTH_SHORT).show();
                    //doStuff
                } else
                {
                    Toast.makeText(FileBrowser.this, browsedFiles.get(position) + "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
