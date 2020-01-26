package com.wintermute.gmassistant.services;

import com.wintermute.gmassistant.model.DirectoryModel;

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
    public List<File> collectTracks(String path)
    {
        List<File> result = new ArrayList<>();
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

    Map<String, List<String>> directoryStructure = new HashMap<>();
    Map<String, Map<String, Object>> nestedDirectories = new HashMap<>();

    private DirectoryModel walkFiles(File path){
        DirectoryModel myDir = new DirectoryModel(path.getName(), path.getPath());
        List<String> files = new ArrayList<>();
        String currentDir = myDir.getName();

        File[] content = new File(myDir.getPath()).listFiles();

        for (File file : content){
            if (file.isDirectory()){

                HashMap<String, Object> tmpMap = new HashMap<>();
                tmpMap.put(file.getName(), Arrays.asList(Objects.requireNonNull(file.listFiles())));
                nestedDirectories.put(currentDir, tmpMap);

                currentDir = file.getName();
                walkFiles(file);
            }
            else {
                files.add(file.getName());
            }
            directoryStructure.put(currentDir, files);
            myDir.setSubDirs(directoryStructure);
        }
        return myDir;
    }

    public String getDirectoryTree(String path) {
        DirectoryModel directoryModel = walkFiles(new File(path));
        return "";
    }
}
