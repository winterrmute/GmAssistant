package com.wintermute.soundboard.client;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.wintermute.soundboard.R;
import com.wintermute.soundboard.adapters.SongAdapter;
import com.wintermute.soundboard.model.Song;
import com.wintermute.soundboard.services.MediaPlayerService;
import com.wintermute.soundboard.services.MediaPlayerService.MusicBinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class interacts with the MediaPlayerService. Allows to start, stop and pause music.
 */
public class MediaPlayer extends AppCompatActivity
{

    private MediaPlayerService mediaPlayerService;
    private Intent playIntent;
    private boolean musicBound = false;

    private ArrayList<Song> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);

        ListView songListView = findViewById(R.id.song_list);
        songList = new ArrayList<>();
        getSongList();

        Collections.sort(songList, new Comparator<Song>(){
            public int compare(Song a, Song b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        SongAdapter songAdapter = new SongAdapter(this, songList);
        songListView.setAdapter(songAdapter);
    }

    /**
     * Fill song list.
     */
    void getSongList(){
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                songList.add(new Song(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }
    }

    private ServiceConnection musicConnection = new ServiceConnection()
    {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service)
        {
            MusicBinder binder = (MusicBinder) service;
            mediaPlayerService = binder.getService();
            mediaPlayerService.setList(songList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name)
        {
            musicBound = false;
        }
    };

    @Override
    protected void onStart()
    {
        super.onStart();
        if (playIntent == null)
        {
            playIntent = new Intent(this, MediaPlayerService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    public void songPicked(View view)
    {
        mediaPlayerService.setSong(Integer.parseInt(view.getTag().toString()));
        mediaPlayerService.playSong();
    }
}
