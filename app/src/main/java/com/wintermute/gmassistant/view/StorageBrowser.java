package com.wintermute.gmassistant.view;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.FileAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Storage browser to access external storage.
 * <p>
 * TODO: refactor to use access framework
 *
 * @author wintermute
 */
public class StorageBrowser extends AppCompatActivity
{

    private File path;
    private ListView fileView;
    private List<File> currentFiles;
    private FileAdapter adapter;
    private ArrayList<String> requestedFiles = new ArrayList<>();
    private boolean recursive = true;
    private boolean startedForSingleTrack;

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
        selectDirectory.setOnClickListener((v) ->
        {
            if (getIntent().getBooleanExtra("createLibrary", false))
            {
                setResult(RESULT_OK, getIntent().putExtra("path", path.getPath()));
                finish();
            } else {
                selectRecursive();
            }
        });
        if (startedForSingleTrack)
        {
            selectDirectory.setVisibility(View.GONE);
        }
    }

    private void handleElement(File selected)
    {
        if (startedForSingleTrack && !selected.isDirectory())
        {
            setResult(RESULT_OK, getIntent().putExtra("path", selected.getPath()));
            finish();
        }
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
    public ArrayList<String> collectTracks(File path, boolean recursive)
    {
        File[] fList = path.listFiles();
        if (fList != null)
        {
            for (File file : fList)
            {
                if (file.isFile())
                {
                    requestedFiles.add(file.getPath());
                } else if (file.isDirectory() && recursive)
                {
                    collectTracks(file, true);
                }
            }
        }
        return requestedFiles;
    }

    private void selectRecursive()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("include subdirectories?");
        builder
            .setMultiChoiceItems(R.array.include_subdirs_value, new boolean[] {true},
                (dialog, which, isChecked) -> recursive = isChecked)
            .setPositiveButton(R.string.ok_result, (dialog, id) ->
            {
                collectTracks(path, recursive);
                setResult(RESULT_OK, getIntent().putStringArrayListExtra("effects", requestedFiles));
                finish();
            })
            .setNegativeButton(R.string.cancel_result, (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void init()
    {
        startedForSingleTrack = getIntent().getBooleanExtra("selectTrack", false);
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
