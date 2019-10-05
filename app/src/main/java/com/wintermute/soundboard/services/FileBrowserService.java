package com.wintermute.soundboard.services;

import com.wintermute.soundboard.model.BrowsedFile;

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
                if (!file.getName().startsWith("."))
                {
                    browsedFiles.add(new BrowsedFile.Builder(file.getName())
                        .withPath(file.getPath())
                        .build());
                }
            }
        }
        return browsedFiles;
    }
}
