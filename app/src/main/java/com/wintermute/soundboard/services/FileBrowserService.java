package com.wintermute.soundboard.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.wintermute.soundboard.client.FileBrowser;
import com.wintermute.soundboard.model.Song;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class FileBrowserService
{
    ArrayList<File> browsedFiles;

    /**
     * Scans directory for files.
     *
     * @return
     */
    public ArrayList<File> scanDir(String path) throws IOException
    {
        File seekPath = new File(path);

        File[] filesList = seekPath.listFiles();
        browsedFiles = new ArrayList<>();

        if (filesList != null)
        {
            for (File file : filesList)
            {
                browsedFiles.add(file);
            }
        }
        return browsedFiles;
    }

    /**
     * Creates Playlist. Will be removed from here.
     * //TODO: Refactor or move me.
     * @param file
     */
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

    /**
     * Extract the audio file duration.
     * TODO: important, but not at this class. Must be moved.
     * @param song
     * @return
     */
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
