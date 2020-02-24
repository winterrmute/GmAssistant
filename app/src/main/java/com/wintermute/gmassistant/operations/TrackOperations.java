package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.helper.TrackDbModel;
import com.wintermute.gmassistant.model.Track;

import java.io.File;
import java.util.Map;

/**
 * This class is responsible for informing views that data has changed, doing basic operations on models and receiving
 * events from views.
 *
 * @author wintermute
 */
public class TrackOperations
{

    private Context ctx;
    private Track track;

    public TrackOperations(Context ctx)
    {
        this.ctx = ctx;
    }

    public Track get(String id)
    {
        TrackDao dao = new TrackDao(ctx);
        Map<String, Object> content = dao.get(TrackDbModel.ID.value(), id);
        return getModel(content);
    }

    /**
     * @param path to audio file on storage
     * @return audio file if exists otherwise create a new created track will be returned
     */
    public Track getTrackOrCreateIfNotExist(String path)
    {
        Track result = checkIfExists(path);
        return result;
    }

    private Track checkIfExists(String path)
    {
        TrackDao dao = new TrackDao(ctx);
        Map<String, Object> stringObjectMap = dao.get(TrackDbModel.PATH.value(), path);
        if (null == stringObjectMap) {
            return newTrack(path);
        }
        return getModel(dao.get(TrackDbModel.PATH.value(), path));
    }

    private Track existingTrack(Map<String, Object> content)
    {
        for (Map.Entry<String, Object> entry : content.entrySet())
        {
            if (null != entry.getValue())
            {
                if (entry.getKey().equals(TrackDbModel.ID.value()))
                {
                    track.setId(Long.parseLong(entry.getValue().toString()));
                }
                if (entry.getKey().equals(TrackDbModel.NAME.value()))
                {
                    track.setName(entry.getValue().toString());
                }
                if (entry.getKey().equals(TrackDbModel.PATH.value()))
                {
                    track.setPath(entry.getValue().toString());
                }
                if (entry.getKey().equals(TrackDbModel.DURATION.value()))
                {
                    track.setDuration((Long) entry.getValue());
                }
                if (entry.getKey().equals(TrackDbModel.ARTIST.value()))
                {
                    track.setArtist(entry.getValue().toString());
                }
            }
        }
        return track;
    }

    private Track newTrack(String path)
    {
        track = new Track();
        track.setName(new File(path).getName());
        track.setPath(path);
        track.setDuration(getDuration(path));
        track.setArtist(getArtist(path));
        storeTrack();
        return track;
    }

    private Track getModel(Map<String, Object> content)
    {
        if (track == null)
        {
            track = new Track();
        }

        if (content == null)
        {
            //TODO:figure out what should happen when data is not present
        } else
        {
            track = existingTrack(content);
        }

        return track;
    }

    public void setTagForTrack(String tag)
    {
        track.setTag(tag);
    }

    private long getDuration(String path)
    {
        return Long.parseLong(extractMetaData(path, MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    private String getArtist(String path)
    {
        return extractMetaData(path, MediaMetadataRetriever.METADATA_KEY_ARTIST);
    }

    private String extractMetaData(String path, int metadataKey)
    {
        MediaMetadataRetriever metadata = new MediaMetadataRetriever();
        metadata.setDataSource(path);
        return metadata.extractMetadata(metadataKey);
    }

    private void storeTrack()
    {
        ContentValues result = new ContentValues();
        result.put(TrackDbModel.NAME.value(), track.getName());
        result.put(TrackDbModel.PATH.value(), track.getPath());
        result.put(TrackDbModel.DURATION.value(), track.getDuration());
        result.put(TrackDbModel.ARTIST.value(), track.getArtist());
        TrackDao dao = new TrackDao(ctx);
        Long insert = dao.insert(result);
        track.setId(insert);
    }
}
