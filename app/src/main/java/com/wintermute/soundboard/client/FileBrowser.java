package com.wintermute.soundboard.client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.adapters.FileAdapter;
import com.wintermute.soundboard.services.FileBrowserService;

import java.io.File;
import java.util.ArrayList;

/**
 * FileBrowser client selecting whole directories or single files to create playlists.
 *
 * @author wintermute
 */
public class FileBrowser extends AppCompatActivity
{

    private final static int RESULT_CODE = 1;
    private File path;
    private ListView fileView;
    private FileBrowserService fileBrowserService;
    private boolean addingNextTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        fileBrowserService = new FileBrowserService();
        getRootDir();
        ArrayList<File> files = fileBrowserService.scanDir(path);
        renderFiles(files);

        Button browseParent = findViewById(R.id.parent_directory);
        browseParent.setOnClickListener(v ->
        {
            if (path.getParent() != null && new File(path.getParent()).canRead())
            {
                path = new File(path.getParent());
                renderFiles(fileBrowserService.scanDir(path));
            } else
            {
                Toast.makeText(FileBrowser.this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        });

        Button selectDirectory = findViewById(R.id.select_directory);
        selectDirectory.setOnClickListener((v) ->
        {
            setResult(RESULT_CODE, new Intent().putExtra("path", path.toString()));
            finish();
        });

        if (addingNextTrack)
        {
            selectDirectory.setVisibility(View.GONE);
        }
    }

    /**
     * Requests the FileBrowserService for content of currently browsed directory.
     *
     * @param dirContent to scan for files and directories
     */
    private void renderFiles(ArrayList<File> dirContent)
    {
        addingNextTrack = getIntent().getBooleanExtra("hasNextTrack", false);

        setListView(dirContent);
        fileView.setOnItemClickListener((parent, view, position, id) ->
        {
            if (addingNextTrack && !dirContent.get(position).isDirectory())
            {
                setResult(RESULT_CODE, new Intent().putExtra("path", dirContent.get(position).getPath()));
                finish();
            } else
            {
                path = new File(dirContent.get(position).getPath());
                boolean isDirectory = new File(dirContent.get(position).getPath()).isDirectory();
                if (isDirectory)
                {
                    renderFiles(fileBrowserService.scanDir(path));
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
