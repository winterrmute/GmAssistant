package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.BoardDbModel;
import com.wintermute.gmassistant.view.model.Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages group (boards) in database.
 *
 * @author wintermute
 */
public class BoardDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public BoardDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
        dbWrite.execSQL("PRAGMA foreign_keys=ON;");
    }

    /**
     * Get Board details.
     *
     * @param category of board.
     * @return selected boards.
     */
    public List<Map<String, Object>> getCategory(String category)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(BoardDbModel.TABLE_NAME.value())
            .append("  WHERE type = '")
            .append(category)
            .append("'");
        return getData(dbRead.rawQuery(query.toString(), null));
    }

    /**
     * Get Board details.
     *
     * @param id of group.
     * @return selected Board.
     */
    public Map<Long, String> get(Long id)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(BoardDbModel.TABLE_NAME.value())
            .append("  WHERE id = '")
            .append(id)
            .append("'");
        return getBoardData(dbRead.rawQuery(query.toString(), null));
    }

    private Map<Long, String> getBoardData(Cursor query)
    {
        Map<Long, String> groups = new HashMap<>();
        while (query.moveToNext())
        {
            Long id = query.getLong(query.getColumnIndex("id"));
            String name = query.getString(query.getColumnIndex("name"));
            groups.put(id, name);
        }
        return groups;
    }

    private List<Map<String, Object>> getData(Cursor query)
    {
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> board;
        while (query.moveToNext())
        {
            board = new HashMap<>();
            for (String attr : BoardDbModel.getValues())
            {
                if (!attr.equals(BoardDbModel.TABLE_NAME.value()))
                {
                    if (attr.equals(BoardDbModel.ID.value()))
                    {
                        board.put(attr, query.getLong(query.getColumnIndex(attr)));
                    } else
                    {
                        board.put(attr, query.getString(query.getColumnIndex(attr)));
                    }
                }
            }
            result.add(board);
        }
        return result;
    }

    /**
     * Creates new board.
     *
     * @param values board name.
     * @return id of created board.
     */
    public Long insert(ContentValues values)
    {
        return dbWrite.insert(BoardDbModel.TABLE_NAME.value(), null, values);
    }

    public void delete(Board board)
    {
        dbWrite.delete(BoardDbModel.TABLE_NAME.value(), BoardDbModel.ID.value() + " = '" + board.getId() + "'",
            new String[] {});
    }
}
