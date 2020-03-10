package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.view.model.Directory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents directory
 */
public class DirectoryDao extends BaseDao
{
    private static final String TABLE_NAME = "library";
    private static final String ID_KEY = "id";
    private static final String PATH_KEY = "path";
    private static final String TAG_KEY = "tag";
    private static final String RECURSIVELY_KEY = "recursively";

    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public DirectoryDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
    }

    /**
     * Create new light scene.
     *
     * @param directory containing selected audio files
     * @return id of new created audio library.
     */
    public long insert(Directory directory)
    {
        Map<String, Object> object = createObject(directory);
        ContentValues values = getContentValues(object);
        return dbWrite.insert(TABLE_NAME, null, values);
    }

    /**
     * @return map containing non null values.
     */
    private Map<String, Object> createObject(Directory target)
    {
        HashMap<String, Object> obj = new HashMap<>();
        obj.put(ID_KEY, target.getId());
        obj.put(PATH_KEY, target.getPath());
        obj.put(TAG_KEY, target.getTag());
        obj.put(RECURSIVELY_KEY, target.getRecursive());
        return removeEmptyValues(obj);
    }

    public Directory getById(String directoryId)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(TABLE_NAME)
            .append(" WHERE id = '")
            .append(directoryId)
            .append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null)).get(0);
    }

    public List<Directory> getDirectoriesForCategory(String tag)
    {
        StringBuilder query =
            new StringBuilder("SELECT * FROM ").append(TABLE_NAME).append(" WHERE tag = '").append(tag).append("'");
        return mapObject(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Translates the data from database to java objects.
     *
     * @param cursor to iterate over database rows.
     * @return list of track objects.
     */
    private ArrayList<Directory> mapObject(Cursor cursor)
    {
        ArrayList<Directory> result = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Directory dir = new Directory();
            dir.setId(Long.parseLong(getKeyValue(cursor, ID_KEY)));
            dir.setPath(getKeyValue(cursor, PATH_KEY));
            dir.setTag(getKeyValue(cursor, TAG_KEY));
            dir.setRecursive(Boolean.parseBoolean(getKeyValue(cursor, RECURSIVELY_KEY)));
            result.add(dir);
        }
        return result;
    }

    /**
     * Safely gets data from database.
     *
     * @param cursor to pick data from database
     * @param column containing value
     * @return value stored in db if possible, otherwise "-1"
     */
    private String getKeyValue(Cursor cursor, String column)
    {
        if (ID_KEY.equals(column) && cursor.getColumnIndex(column) != -1)
        {
            return String.valueOf(cursor.getLong(cursor.getColumnIndex(column)));
        } else if (cursor.getColumnIndex(column) != -1)
        {
            return cursor.getString(cursor.getColumnIndex(column));
        }
        return "-1";
    }
}
