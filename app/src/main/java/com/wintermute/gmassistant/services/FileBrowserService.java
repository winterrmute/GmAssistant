package com.wintermute.gmassistant.services;

import com.wintermute.gmassistant.model.LibraryElement;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileBrowserService
{
    private final static String PREVIOUS_DIRECTORY = "previous directory";
    private String rootPath;

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
     * @param path to scan directory
     * @return list of files for element from library
     */
    private List<LibraryElement> getFiles(String path)
    {
        return Arrays
            .stream(Objects.requireNonNull(new File(path).listFiles()))
            .map(f -> new LibraryElement(f.getName(), f.getPath(), false))
            .collect(Collectors.toList());
    }

    public List<LibraryElement> browseLibrary(LibraryElement target, List<LibraryElement> rootElements)
    {
        if (new File(target.getPath()).isDirectory())
        {
            List<LibraryElement> newContent;
            String newPath;
            if (target.isRoot())
            {
                rootPath = target.getPath();
            }

            if (PREVIOUS_DIRECTORY.equals(target.getName()))
            {
                newPath = target.getPath().substring(0, target.getPath().lastIndexOf('/'));
            } else
            {
                newPath = target.getPath();
            }
            LibraryElement goToParent = new LibraryElement(PREVIOUS_DIRECTORY, newPath, false);

            if (target.getPath().equals(rootPath) && !target.isRoot())
            {
                newContent = rootElements;
            } else
            {
                newContent = getFiles(newPath);
                newContent.add(0, goToParent);
                return newContent;
            }
            return new ArrayList<>(newContent);
        }
        return Collections.singletonList(target);
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
