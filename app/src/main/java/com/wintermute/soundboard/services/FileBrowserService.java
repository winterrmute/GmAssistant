package com.wintermute.soundboard.services;

import android.content.Context;
import com.wintermute.soundboard.model.BrowsedFile;
import com.wintermute.soundboard.model.Track;
import com.wintermute.soundboard.services.database.dao.TrackDao;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                    browsedFiles.add(new BrowsedFile.Builder(file.getName()).withPath(file.getPath()).build());
                }
            }
        }
        return browsedFiles;
    }

    public List<Track> collectTracks(Context ctx, String path)
    {

        List<Track> result = new ArrayList<>();
        File[] files = new File(path).listFiles();

        if (files != null)
        {
            for (File file : files)
            {
                if (file.isDirectory())
                {
                    collectTracks(ctx, file.getPath());
                } else if (file.toString().endsWith(".mp3"))
                {
                    mapTracks(ctx, file);
                }
            }
        }
        return result;
    }

    public void mapTracks(Context ctx, File file)
    {
        Track result = new Track();

//        result.setId(UUID.randomUUID().getMostSignificantBits());
        result.setName(file.getName());
        result.setArtist("");
        result.setPath(file.getPath());
        result.setScene_id("0");

        TrackDao trackDao = new TrackDao(ctx);
        trackDao.insert(result);
    }
}
