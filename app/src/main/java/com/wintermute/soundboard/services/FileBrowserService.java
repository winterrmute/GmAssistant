package com.wintermute.soundboard.services;

import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.widget.Toast;
import com.wintermute.soundboard.client.FileBrowser;
import com.wintermute.soundboard.model.Song;

import java.io.File;
import java.util.ArrayList;

public class FileBrowserService
{
    ArrayList<File> browsedFiles;

    /**
     * Scans directory for files.
     *
     * @return
     */
    public ArrayList<File> scanDir(String seekPath)
    {

//        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File[] filesList = path.listFiles();
        browsedFiles = new ArrayList<>();

        if (filesList != null) {
            for (File file : filesList){
                browsedFiles.add(file);
            }
        }
        return browsedFiles;
    }

    //TODO: Refactor or move me.
    private void addToPlaylist(File file)
    {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(file.getPath());

        Song song = new Song.Builder(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE))
            .withArtist(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST))
            .withPath(file.getPath())
            .withDuration(getSongDuration(file))
            .build();
//        browsedFiles.add(song.toString());
    }

    private long getSongDuration(File song)
    {
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(song.getPath());

        long duration = Long.parseLong(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        //TODO: not sure if stupid or important
        String seconds = String.valueOf((duration % 60000) / 1000);
        String minutes = String.valueOf(duration / 60000);
        if (Integer.parseInt(minutes) < 10)
        {
            minutes = "0" + minutes;
        }
        if (Integer.parseInt(seconds) < 10)
        {
            seconds = "0" + seconds;
        }
        String songDuration = new StringBuilder().append(minutes).append(":").append(seconds).toString();

        return duration;
    }
}
