package com.wintermute.soundboard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.soundboard.adapters.PlaylistAdapter;
import com.wintermute.soundboard.client.FileBrowser;
import com.wintermute.soundboard.dialogs.CreatePlaylist;
import com.wintermute.soundboard.model.Playlist;
import com.wintermute.soundboard.services.database.DbManager;

import java.util.ArrayList;

/**
 * User management panel.
 *
 * @author wintermute
 */
public class Soundboard extends AppCompatActivity
{
    private ListView playlists;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soundboard);

        init();

//        Button browseFiles = findViewById(R.id.browse_files);
//        browseFiles.setOnClickListener(v ->
//        {
//            Intent fileBrowser = new Intent(Soundboard.this, FileBrowser.class);
//            startActivity(fileBrowser);
//        });

        Button playlist = findViewById(R.id.playlist);
        playlist.setOnClickListener(v ->
        {
            Intent createPlaylist = new Intent(Soundboard.this, CreatePlaylist.class);
            startActivity(createPlaylist);

            finish();
            startActivity(getIntent());
        });
    }


    /**
     * Grants permissions to the application to access the storage.
     */
    void grantUserPermission()
    {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE))
            {
            }
            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            return;
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE))
            {
            }
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            return;
        }

    }

    void renderPlaylists()
    {
        SQLiteDatabase db = new DbManager(this).getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT name FROM user_playlist", null);
        ArrayList<String> userList = new ArrayList<>();
        while (cursor.moveToNext()) {
            userList.add(cursor.getString(cursor.getColumnIndexOrThrow("name")));
        }
        if (userList != null) {
            PlaylistAdapter playlistAdapter = new PlaylistAdapter(this, userList);
            playlists.setAdapter(playlistAdapter);
        }
    }

    void init()
    {
        grantUserPermission();
        playlists = findViewById(R.id.playlists);
        renderPlaylists();
    }
}
