package com.wintermute.gmassistant.services;

import com.wintermute.gmassistant.model.LibraryFile;

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
     * @param path to scan directory
     * @return list of files for element from library
     */
    private List<LibraryFile> getFiles(String path)
    {
        return Arrays
            .stream(Objects.requireNonNull(new File(path).listFiles()))
            .map(f -> new LibraryFile(f.getName(), f.getPath(), false))
            .collect(Collectors.toList());
    }

    public List<LibraryFile> browseLibrary(LibraryFile target, List<LibraryFile> rootElements)
    {
        if (new File(target.getPath()).isDirectory())
        {
            if (target.isRoot())
            {
                rootPath = target.getPath();
            }

            String newPath;
            if (PREVIOUS_DIRECTORY.equals(target.getName()))
            {
                newPath = target.getPath().substring(0, target.getPath().lastIndexOf('/'));
            } else
            {
                newPath = target.getPath();
            }
            LibraryFile goToParent = new LibraryFile(PREVIOUS_DIRECTORY, newPath, false);

            List<LibraryFile> newContent;
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
