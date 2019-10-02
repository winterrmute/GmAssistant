package com.wintermute.soundboard.client;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.adapters.FileAdapter;
import com.wintermute.soundboard.model.BrowsedFile;
import com.wintermute.soundboard.model.Song;
import com.wintermute.soundboard.services.FileBrowserService;
import com.wintermute.soundboard.services.database.DatabaseConnector;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

        path = this.getExternalFilesDir("");
        renderFiles(path.toString());
        browseParent();

        Button selectDirectory = findViewById(R.id.select_directory);
        selectDirectory.setOnClickListener((v) ->
        {
            ArrayList<BrowsedFile> selectedFiles = fileBrowserService.scanDir(path.toString());
            selectedFiles.stream().forEach(e -> dbConnection(e));
        });
    }

    /**
     * TODO: Refactor me. I am not ready.
     * @param song
     */
    private void dbConnection(BrowsedFile song)
    {
        DatabaseConnector dbc = new DatabaseConnector(this);
        SQLiteDatabase db = dbc.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("song", song.getName());
        values.put("path", song.getPath());
        long newRowId = db.insert("playlist", null, values);

        readDatabase();
    }

    /**
     * TODO: Refactor me. I was only for test purposes.
     */
    private void readDatabase()
    {
        DatabaseConnector dbc = new DatabaseConnector(this);
        SQLiteDatabase db = dbc.getReadableDatabase();

        String[] projection = {"id", "song", "path"};

        String selection = "song = ?";
        String[] selectionArgs = {"CoJG Hunt Sneak.mp3"};

        String sortOrder = "song DESC";


        Cursor cursor = db.query("playlist", projection, selection, selectionArgs, null, null, sortOrder);
        String data[] = new String[cursor.getCount()];
        cursor.moveToFirst();
        int i = 0;
        while (!cursor.isAfterLast()) {
            data[i] = cursor.getString(cursor.getColumnIndex("song"));
            System.out.println(data[i]);
            cursor.moveToNext();
            i ++;
        }

    }

    /**
     * Checks whether the parent directory is accessible. Requests the FileBrowserService for content of parent
     * directory.
     */
    void browseParent()
    {
        Button explore_parent = findViewById(R.id.parent_directory);
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
        fileBrowserService = new FileBrowserService();
        ArrayList<BrowsedFile> browsedFiles = fileBrowserService.scanDir(targetPath);
        setListView(browsedFiles);

        fileView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        fileView.setOnItemClickListener(((parent, view, position, id) ->
        {
            if (Paths.get(browsedFiles.get(position).getPath()).toFile().isDirectory())
            {
                renderFiles(browsedFiles.get(position).getPath());
                path = Paths.get(browsedFiles.get(position).getPath()).toFile();
            } else
            {
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
