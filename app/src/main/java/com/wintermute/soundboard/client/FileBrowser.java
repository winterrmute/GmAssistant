package com.wintermute.soundboard.client;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.adapters.FileAdapter;
import com.wintermute.soundboard.model.BrowsedFile;
import com.wintermute.soundboard.services.FileBrowserService;

import java.io.File;
import java.nio.file.Paths;
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

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        path = this.getExternalFilesDir("");
        renderFiles(path.toString());
        browseParent();
    }

    /**
     * Checks whether the parent directory is accessible. Requests the FileBrowserService for content of parent
     * directory.
     */
    void browseParent()
    {
        Button explore_parent = findViewById(R.id.parent_content);
        explore_parent.setOnClickListener(v ->
        {
            if (path.getParent() != null)
            {
                if (Paths.get(path.getParent()).toFile().canRead())
                {
                    renderFiles(path.getParent());
                    path = Paths.get(path.getParent()).toFile();
                } else
                {
                    Toast.makeText(FileBrowser.this, "Permission denied!", Toast.LENGTH_SHORT).show();
                }
            } else
            {
                Toast.makeText(FileBrowser.this, "Operation not possible", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Requests the FileBrowserService for content of currently browsed directory.
     *
     * @param targetPath to scan for files and directories
     */
    void renderFiles(String targetPath)
    {
        FileBrowserService fileBrowserService = new FileBrowserService();
        ArrayList<BrowsedFile> browsedFiles = fileBrowserService.scanDir(targetPath);
        setListView(browsedFiles);

        fileView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        fileView.setOnItemClickListener(((parent, view, position, id) ->
        {
            if (Paths.get(browsedFiles.get(position).getPath()).toFile().isDirectory())
            {
                renderFiles(browsedFiles.get(position).getPath());
                path = Paths.get(browsedFiles.get(position).getPath()).toFile();
            }
            else {
                browsedFiles.get(position).setChecked(!browsedFiles.get(position).getCheckStatus());
                System.out.println(browsedFiles.get(position).getCheckStatus());

            }
        }));

    }

    /**
     * Creates the listView and sets the {@link FileAdapter}.
     *
     * @param browsedFiles to render in the listView.
     */
    private void setListView(ArrayList<BrowsedFile> browsedFiles)
    {
        fileView = findViewById(R.id.files_list);
        FileAdapter fileAdapter = new FileAdapter(this, browsedFiles);
        fileView.setAdapter(fileAdapter);
    }
}
