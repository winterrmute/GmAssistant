package com.wintermute.gmassistant.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.FileAdapter;
import com.wintermute.gmassistant.services.FileBrowserService;

import java.io.File;
import java.util.ArrayList;

/**
 * FileBrowser client selecting whole directories or single files to create playlists.
 *
 * @author wintermute
 */
public class FileBrowser extends AppCompatActivity
{

    private File path;
    private ListView fileView;
    private FileBrowserService fileBrowserService;
    private boolean addingNextTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        init();

        Button browseParent = findViewById(R.id.parent_directory);
        browseParent.setOnClickListener(v ->
        {
            if (path.getParent() != null && new File(path.getParent()).canRead())
            {
                path = new File(path.getParent());
                browseOrSelectFiles(fileBrowserService.scanDir(path));
            } else
            {
                Toast.makeText(FileBrowser.this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        });

        Button selectDirectory = findViewById(R.id.select_current_directory);
        selectDirectory.setOnClickListener((v) ->
        {
            setResult(RESULT_OK, new Intent().putExtra("path", path.toString()));
            finish();
        });

        if (addingNextTrack)
        {
            selectDirectory.setVisibility(View.GONE);
        }
    }

    private void init()
    {
        fileBrowserService = new FileBrowserService();
        getRootDir();
        ArrayList<File> files = fileBrowserService.scanDir(path);
        browseOrSelectFiles(files);
    }

    /**
     * Requests the FileBrowserService for content of currently browsed directory.
     *
     * @param dirContent to scan for files and directories
     */
    private void browseOrSelectFiles(ArrayList<File> dirContent)
    {
        addingNextTrack = getIntent().getBooleanExtra("selectTrack", false);

        setListView(dirContent);
        fileView.setOnItemClickListener((parent, view, position, id) ->
        {
            if (dirContent.get(position).isDirectory())
            {
                path = new File(dirContent.get(position).getPath());
                browseOrSelectFiles(fileBrowserService.scanDir(path));
            } else
            {
                if (addingNextTrack)
                {
                    path = new File(dirContent.get(position).getPath());
                    setResult(RESULT_OK, new Intent().putExtra("path", path.toString()));
                    finish();
                }
            }
        });
    }

    /**
     * Creates the listView and sets the {@link FileAdapter}.
     *
     * @param browsedFiles to render in the listView.
     */
    private void setListView(ArrayList<File> browsedFiles)
    {
        fileView = findViewById(R.id.files_list);
        FileAdapter fileAdapter = new FileAdapter(this, browsedFiles);
        fileView.setAdapter(fileAdapter);
    }

    /**
     * Starts browsing in root directory instead of app directory.
     */
    private void getRootDir()
    {
        path = this.getExternalFilesDir("");
        while (path.getParent() != null && new File(path.getParent()).canRead())
        {
            path = new File(path.getParent());
        }
    }
}
