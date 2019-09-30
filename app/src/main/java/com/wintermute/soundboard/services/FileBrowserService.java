package com.wintermute.soundboard.services;

import android.media.MediaMetadataRetriever;
import com.wintermute.soundboard.model.BrowsedFile;
import com.wintermute.soundboard.model.Song;

import java.io.File;
import java.util.ArrayList;

public class FileBrowserService
{
    ArrayList<BrowsedFile> browsedFiles;

    /**
     * Scans directory for files.
     *
     * @return
     */
    public ArrayList<BrowsedFile> scanDir(String path)
    {
        File[] filesList = new File(path).listFiles();

        if (filesList != null)
        {
            browsedFiles = new ArrayList<>();
            for (File file : filesList)
            {
                browsedFiles.add(new BrowsedFile.Builder(file.getName()).withPath(file.getPath()).build());
            }
        }
        return browsedFiles;
    }

    /**
     * Creates Playlist. Will be removed from here. //TODO: Refactor or move me.
     *
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
     * Extract the audio file duration. TODO: important, but not at this class. Must be moved.
     *
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
