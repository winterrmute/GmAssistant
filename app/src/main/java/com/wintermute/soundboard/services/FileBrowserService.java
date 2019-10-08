package com.wintermute.soundboard.services;

import android.content.Context;
import com.wintermute.soundboard.model.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileBrowserService
{
    private Context ctx;

    /**
     * Creates an instance with context of activity which called this service.
     *
     * @param ctx context of activity which called this class.
     */
    public FileBrowserService(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * Scans directory for files.
     *
     * @return list of BrowsedFiles
     */
    public ArrayList<File> scanDir(String path)
    {
        ArrayList<File> browsedFiles = new ArrayList<>();
        File[] filesList = new File(path).listFiles();

        if (filesList != null)
        {
            browsedFiles = new ArrayList<>();
            for (File file : filesList)
            {
                if (!file.getName().startsWith("."))
                {
                    browsedFiles.add(file);
                }
            }
        }
        return browsedFiles;
    }

    /**
     * Scans directory for audio tracks.
     *
     * @param path to browse for files.
     * @return list of found tracks.
     */
    public List<Track> collectTracks(String path)
    {
        List<Track> result = new ArrayList<>();
        File[] files = new File(path).listFiles();

        if (files != null)
        {
            for (File file : files)
            {
                if (file.isDirectory())
                {
                    collectTracks(file.getPath());
                } else if (file.toString().endsWith(".mp3") || file.toString().endsWith(".wav"))
                {
                    result.add(storeTracks(file));
                }
            }
        }
        return result;
    }


    //TODO: do something with me
    private Track storeTracks(File file)
    {
        Track result = new Track();
        result.setName(file.getName());
        result.setArtist("");
        result.setPath(file.getPath());
        result.setScene_id("0");
        return result;
    }
}
