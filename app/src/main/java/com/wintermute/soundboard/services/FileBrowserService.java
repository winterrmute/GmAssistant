package com.wintermute.soundboard.services;

import com.wintermute.soundboard.model.Track;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileBrowserService
{
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
    public List<File> collectTracks(String path)
    {
        List<File> result = new ArrayList<>();
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
                    result.add(file);
                }
            }
        }
        return result;
    }
}
