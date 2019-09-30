package com.wintermute.soundboard.client;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.services.FileBrowserService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileBrowser extends AppCompatActivity
{

    File path;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        path = this.getExternalFilesDir("");

        try
        {
            renderFilesAsList(path.toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        browseParent();
    }

    /**
     * Requests the FileBrowserService for content of parent directory.
     */
    void browseParent()
    {
        Button explore_parent = findViewById(R.id.parent_content);
        explore_parent.setOnClickListener(v -> browseParent());
        path = new File(path.getParent());
        try
        {
            renderFilesAsList(path.toString());
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Requests the FileBrowserService for content of currently browsed directory.
     *
     * @param targetPath to scan for files and directories
     * @throws IOException
     */
    void renderFilesAsList(String targetPath) throws IOException
    {
        FileBrowserService fileBrowserService = new FileBrowserService();
        ArrayList<File> browsedFiles = fileBrowserService.scanDir(targetPath);

        ListView dirContent = findViewById(R.id.dir_content);
        ArrayAdapter adapter =
            new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_2, android.R.id.text1, browsedFiles);
        dirContent.setAdapter(adapter);

        dirContent.setOnItemClickListener((parent, view, position, id) ->
        {
            if (browsedFiles.get(position).isDirectory())
            {
                try
                {
                    renderFilesAsList(browsedFiles.get(position).getAbsolutePath());
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            } else
            {
                Toast.makeText(FileBrowser.this, browsedFiles.get(position) + "", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
