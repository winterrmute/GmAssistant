package com.wintermute.soundboard.dialogs;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.model.Playlist;
import com.wintermute.soundboard.services.database.DbManager;

import java.util.UUID;

public class CreatePlaylist extends AppCompatActivity
{

    EditText playlistName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);

        playlistName = findViewById(R.id.playlist_name);

        Button submit = findViewById(R.id.submit);

        submit.setOnClickListener(v -> {
            createUserPlaylist();
            this.finish();
        });
    }

    void createUserPlaylist(){
        DbManager dbManager = new DbManager(this);
        SQLiteDatabase db = new DbManager(this).getWritableDatabase();

        Playlist playlist = new Playlist();
        playlist.setId(UUID.randomUUID().getMostSignificantBits());
        playlist.setName(playlistName.getText().toString());

        ContentValues values = new ContentValues();
        values.put("id", playlist.getId());
        values.put("name", playlist.getName());

        db.insert("user_playlist", null, values);
    }
}
