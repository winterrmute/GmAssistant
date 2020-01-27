package com.wintermute.gmassistant.client;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.FileAdapter;
import com.wintermute.gmassistant.services.FileBrowserService;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * FileBrowser client selecting whole directories or single files to create playlists.
 *
 * @author wintermute
 */
public class FileBrowser extends AppCompatActivity
{

    private File path;
    private ListView fileView;
    private FileAdapter fileAdapter;
    private FileBrowserService fileBrowserService;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        init();

        Button browseParent = findViewById(R.id.parent_directory);
        browseParent.setOnClickListener(v -> browseParentDirectory());

        Button selectDirectory = findViewById(R.id.select_current_directory);
        selectDirectory.setOnClickListener((v) ->
        {
            //TODO: implement alert
            setResult(RESULT_OK, new Intent().putExtra("path", path.toString()).putExtra("includingSubdirs", false));
            finish();
        });
    }

    private void browseParentDirectory()
    {
        if (fileBrowserService.checkPermission(path)){
            path = new File(path.getParent());
            browseFiles();
        } else {
            Toast.makeText(FileBrowser.this, "Permission denied!", Toast.LENGTH_SHORT).show();
        }
    }

    private void init()
    {
        fileBrowserService = new FileBrowserService();
        getRootDir();
        ArrayList<File> files = fileBrowserService.scanDir(path);
        initListView(files);
        browseFiles();
    }

    /**
     * Shows files in current directory.
     */
    private void browseFiles(){
        ArrayList<File> files= fileBrowserService.scanDir(path);
        fileAdapter.updateDisplayedElements(files);

        fileView.setOnItemClickListener(((parent, view, position, id) -> {
            if (files.get(position).isDirectory()){
                path = files.get(position);
                browseFiles();
            }
        }));
    }

    /**
     * Creates the listView and sets the {@link FileAdapter}.
     *
     * @param browsedFiles to render in the listView.
     */
    private void initListView(ArrayList<File> browsedFiles)
    {
        fileView = findViewById(R.id.files_list);
        fileAdapter = new FileAdapter(this, browsedFiles);
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
