package com.wintermute.gmassistant.services;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    /**
     * Scans directory for audio tracks.
     *
     * @param path to browse for files.
     * @return list of found tracks.
     */
    public List<String> collectTracks(String path)
    {
        List<String> result = new ArrayList<>();
        File[] fList = new File(path).listFiles();
        if (fList != null)
        {
            for (File file : fList)
            {
                if (file.isFile())
                {
                    result.add(file.getName());
                } else if (file.isDirectory())
                {
                    collectTracks(file.getAbsolutePath());
                }
            }
        }
        return result;
    }

    /**
     * Checks if browsing given directory is allowed.
     *
     * @param path to browse
     * @return true if is allowed otherwise false
     */
    public boolean checkPermission(File path)
    {
        return path.getParent() != null && new File(path.getParent()).canRead();
    }

    public String getDirectoryTree(String path) {
        return "";
    }
}
