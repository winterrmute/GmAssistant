package com.wintermute.gmassistant.client;

import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.FileAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Storage browser to access external storage.
 *
 * TODO: refactor to use access framework
 * @author wintermute
 */
public class StorageBrowser extends AppCompatActivity
{

    private File path;
    private ListView fileView;
    private List<File> currentFiles;
    private FileAdapter adapter;
    private List<String> callback = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_browser);

        init();

        fileView.setOnItemClickListener((parent, view, position, id) -> handleElement(currentFiles.get(position)));

        Button browseParent = findViewById(R.id.parent_directory);
        browseParent.setOnClickListener(v -> browseParent());

        Button selectDirectory = findViewById(R.id.select_current_directory);
        selectDirectory.setOnClickListener((v) -> {
            ArrayList<String> effects = new ArrayList<>(collectTracks(path));
            setResult(RESULT_OK, getIntent().putStringArrayListExtra("effects", effects));
            finish();
        });
    }

    private void handleElement(File selected)
    {
        path = selected;
        if (selected.isDirectory())
        {
            currentFiles = getFiles();
        }
        adapter.updateDisplayedElements(currentFiles);
    }

    private List<File> getFiles()
    {
        if (path.listFiles() != null)
        {
            return Arrays.asList(path.listFiles());
        } else
        {
            return new ArrayList<>();
        }
    }

    /**
     * Scans directory for audio tracks.
     *
     * @param path to browse for files.
     * @return list of found tracks.
     */
    public List<String> collectTracks(File path)
    {
        File[] fList = path.listFiles();
        if (fList != null)
        {
            for (File file : fList)
            {
                if (file.isFile())
                {
                    callback.add(file.getPath());
                } else if (file.isDirectory())
                {
                    collectTracks(file);
                }
            }
        }
        return callback;
    }

    private void init()
    {
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
        currentFiles = new ArrayList<>(getFiles());
        initListView(currentFiles);
    }

    private void browseParent()
    {
        if (path.getParentFile() != null && path.getParentFile().canRead())
        {
            { path = path.getParentFile(); }
            adapter.updateDisplayedElements(currentFiles = getFiles());
        } else
        {
            Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Creates the listView and sets the {@link FileAdapter}.
     *
     * @param browsedFiles to render in the listView.
     */
    private void initListView(List<File> browsedFiles)
    {
        fileView = findViewById(R.id.files_list);
        adapter = new FileAdapter(this, browsedFiles);
        fileView.setAdapter(adapter);
    }
}
