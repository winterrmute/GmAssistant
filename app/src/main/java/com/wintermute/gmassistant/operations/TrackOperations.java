package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.model.TrackDbModel;
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

    public Track get(Long id)
    {
        TrackDao dao = new TrackDao(ctx);
        Map<String, Object> content = dao.get(id);
        return getModel(content);
    }

    private Track existingTrack(Map<String, Object> content)
    {
        Track track = new Track();
        for (Map.Entry<String, Object> entry : content.entrySet())
        {
            if (null != entry.getValue())
            {
                if (entry.getKey().equals(TrackDbModel.ID.value()))
                {
                    track.setId((Long) entry.getValue());
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

    /**
     * Create new track model
     *
     * @param path to audio file on storage
     * @return track model
     */
    public Track createTrack(String path)
    {
        track = new Track();
        track.setName(new File(path).getName());
        track.setPath(path);
        track.setDuration(getDuration(path));
        track.setArtist(getArtist(path));
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

    /**
     * Stores new track into database.
     *
     * @param track to store.
     */
    public Long storeTrackIfNotExist(Track track)
    {
        TrackDao dao = new TrackDao(ctx);
        if (trackExists(track, dao))
        {
            track.setId(getModel(dao.getByAttribute(TrackDbModel.PATH.value(), track.getPath())).getId());
        } else
        {
            ContentValues result = new ContentValues();
            result.put(TrackDbModel.NAME.value(), track.getName());
            result.put(TrackDbModel.PATH.value(), track.getPath());
            result.put(TrackDbModel.DURATION.value(), track.getDuration());
            result.put(TrackDbModel.ARTIST.value(), track.getArtist());
            Long insert = dao.insert(result);
            track.setId(insert);
        }
        return track.getId();
    }

    private boolean trackExists(Track track, TrackDao dao)
    {
        return dao.getByAttribute(TrackDbModel.PATH.value(), track.getPath()) != null;
    }
}
