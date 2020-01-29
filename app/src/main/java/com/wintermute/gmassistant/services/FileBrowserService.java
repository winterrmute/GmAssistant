package com.wintermute.gmassistant.services;

import com.wintermute.gmassistant.client.FileBrowser;
import com.wintermute.gmassistant.model.FileElement;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
     * Scans directory for files and directories
     *
     * @param file to browse for files.
     * @return path of selected file.
     */
    private String browseFiles(FileElement file)
    {
        if ("previous directory".equals(file.getName()) && !file.isRoot())
        {
            return file.getPath();
        } else if (new File(file.getPath()).isDirectory())
        {
            return file.getPath();
        } else
        {
            return file.getName();
        }
    }

    public Map<String, List<FileElement>> getFiles(FileElement file)
    {
        List<FileElement> foundFiles = Arrays
            .asList(Objects.requireNonNull(new File(browseFiles(file)).listFiles()))
            .stream()
            .map(f -> new FileElement(f.getName(), f.getPath(), false))
            .collect(Collectors.toList());
        Map<String, List<FileElement>> result = new HashMap<>();
        result.put(new File(file.getPath()).getParent(), foundFiles);
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
}
