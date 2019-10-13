package com.wintermute.soundboard.client;

import android.content.Intent;
import android.os.Bundle;
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

    private File path;
    private ListView fileView;
    FileBrowserService fileBrowserService;

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
            setResult(1, new Intent().putExtra("path", path.toString()));
            finish();
        });
    }

    /**
     * Requests the FileBrowserService for content of currently browsed directory.
     *
     * @param dirContent to scan for files and directories
     */
    private void renderFiles(ArrayList<File> dirContent)
    {
        setListView(dirContent);
        fileView.setOnItemClickListener((parent, view, position, id) ->
        {
            path = new File(dirContent.get(position).getPath());
            if (new File(dirContent.get(position).getPath()).isDirectory())
            {
                renderFiles(fileBrowserService.scanDir(path));
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
     *
     * @return root directory
     */
    private File getRootDir(){
        path = this.getExternalFilesDir("");
        while (path.getParent() != null && new File(path.getParent()).canRead()){
            path = new File(path.getParent());
        }
        return path;
    }

}
