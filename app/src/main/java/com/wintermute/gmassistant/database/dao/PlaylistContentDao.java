package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.SceneDbModel;
import com.wintermute.gmassistant.view.model.PlaylistContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistContentDao extends BaseDao
{
    private static final String TABLE_NAME = "playlist_content";
    private static final String PLAYLIST_KEY = "playlist";
    private static final String TRACK_KEY = "track";
    private static final String SCENE_KEY = "scene";

    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public PlaylistContentDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    /**
     * @return map containing non null values.
     */
    private Map<String, Object> createObject(PlaylistContent content)
    {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put(PLAYLIST_KEY, content.getPlaylist());
        obj.put(TRACK_KEY, content.getTrack());
        obj.put(SCENE_KEY, content.getScene());
        return removeEmptyValues(obj);
    }

    /**
     * Insert row into playlist_content table.
     *
     * @return id of inserted element.
     */
    public void insert(PlaylistContent content)
    {
        Map<String, Object> object = createObject(content);
        ContentValues values = getContentValues(object);
        getContentValues(object);
        dbWrite.insert(TABLE_NAME, null, values);
    }

    /**
     * Select item by id.
     *
     * @param playlistId to identify database entry by id.
     * @return selected track.
     */
    public List<PlaylistContent> getPlaylistContent(String playlistId)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append("  WHERE ")
            .append(PLAYLIST_KEY)
            .append("  =  '")
            .append(playlistId)
            .append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * TODO: REFACTOR ME
     *
     * @return
     */
    public String getSceneId(String playlistId, String trackId)
    {
        String query =
            "SELECT * FROM playlist_content WHERE playlist = '" + playlistId + "' AND track = '" + trackId + "'";

        Cursor cursor = dbRead.rawQuery(query, null);
        return mapObject(cursor).get(0).getScene();
    }

    /**
     * Get all records from table track as list.
     *
     * @return list of track names.
     */
    public ArrayList<PlaylistContent> getAll()
    {
        StringBuilder query = new StringBuilder("SELECT * FROM " + TABLE_NAME);
        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Translates the data from database to java objects.
     *
     * @param cursor to iterate over database rows.
     * @return list of track objects.
     */
    private ArrayList<PlaylistContent> mapObject(Cursor cursor)
    {
        ArrayList<PlaylistContent> result = new ArrayList<>();
        while (cursor.moveToNext())
        {
            PlaylistContent content = new PlaylistContent();
            content.setPlaylist(getValue(cursor, PLAYLIST_KEY));
            content.setTrack(getValue(cursor, TRACK_KEY));
            content.setScene(getValue(cursor, SCENE_KEY));
            result.add(content);
        }
        return result;
    }

    /**
     * Safely gets data from database.
     *
     * @param cursor to pick data from database
     * @param key containing value
     * @return value stored in db if possible, otherwise "-1"
     */
    private String getValue(Cursor cursor, String key)
    {
        if (cursor.getColumnIndex(key) != -1)
        {
            return cursor.getString(cursor.getColumnIndex(key));
        }
        return "-1";
    }

    public void updateScene(String sceneId, String playlistId, String trackId)
    {
        StringBuilder query = new StringBuilder("UPDATE ")
            .append(TABLE_NAME)
            .append(" SET ")
            .append(SCENE_KEY)
            .append(" = '")
            .append(sceneId)
            .append("' WHERE ")
            .append(PLAYLIST_KEY)
            .append(" = '")
            .append(playlistId)
            .append("' AND ")
            .append(TRACK_KEY)
            .append(" = '")
            .append(trackId)
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * @param playlist to get its content
     * @param trackId to identify from playlist content
     * @return sceneId for selected playlist
     */
    public String getSceneIdForTrackInPlaylist(String playlist, String trackId)
    {
        StringBuilder query = new StringBuilder("SELECT scene FROM ")
            .append(TABLE_NAME)
            .append(" WHERE playlist ='")
            .append(playlist)
            .append("' AND track ='")
            .append(trackId)
            .append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null)).get(0).getScene();
    }

    /**
     * Deletes row from database by id.
     *
     * @param playlistId of playlist to remove its content.
     */
    public void deleteByPlaylistId(String playlistId)
    {
        dbWrite.execSQL(getDeleteQuery(TABLE_NAME, PLAYLIST_KEY, playlistId));
    }

    /**
     * Deletes row from database by id.
     *
     * @param trackId to remove from database.
     */
    public void deleteTrackFromPlaylist(String playlistId, String trackId)
    {
        StringBuilder query = new StringBuilder("DELETE FROM ")
            .append(TABLE_NAME)
            .append(" WHERE ")
            .append(PLAYLIST_KEY)
            .append(" = '")
            .append(playlistId)
            .append(" ' AND ")
            .append(TRACK_KEY)
            .append(" = '")
            .append(trackId)
            .append("'");
        dbWrite.execSQL(query.toString());
    }

    /**
     * removes deleted scene from playlist content.
     */
    public void deleteScene(Long sceneId)
    {
        dbWrite.delete(SceneDbModel.TABLE_NAME.value(), SceneDbModel.ID.value() + " = " + sceneId,
            new String[] {});
    }
}
