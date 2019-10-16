package com.wintermute.soundboard.services;

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
    public ArrayList<File> scanDir(File path)
    {
        ArrayList<File> browsedFiles = new ArrayList<>();
        File[] filesList = new File(path.toString()).listFiles();

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

    private List<File> result = new ArrayList<>();

    /**
     * Scans directory for audio tracks.
     *
     * @param path to browse for files.
     * @return list of found tracks.
     */
    public List<File> collectTracks(String path)
    {
        File[] fList = new File(path).listFiles();
        if (fList != null)
        {
            for (File file : fList)
            {
                if (file.isFile())
                {
                    result.add(file);
                } else if (file.isDirectory())
                {
                    collectTracks(file.getAbsolutePath());
                }
            }
        }
        return result;
    }
}
