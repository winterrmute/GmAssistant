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
     * Creates new board.
     *
     * @param values board name.
     * @return id of created board.
     */
    public Long insert(ContentValues values)
    {
        return dbWrite.insert(BoardDbModel.TABLE_NAME.value(), null, values);
    }

    /**
     * Deletes board.
     *
     * @param board to remove.
     */
    public void delete(Board board)
    {
        dbWrite.delete(BoardDbModel.TABLE_NAME.value(), BoardDbModel.ID.value() + " = '" + board.getId() + "'",
            new String[] {});
    }

    /**
     * Update board
     *
     * @param values to update in board.
     * @param boardId to update.
     */
    public void update(ContentValues values, Long boardId)
    {
        dbWrite.update(BoardDbModel.TABLE_NAME.value(), values, BoardDbModel.ID.value() + " = " + boardId,
            new String[] {});
    }

    /**
     * @param boardId of board to get.
     * @return data of board by id.
     */
    public Map<String, Object> get(Long boardId)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(BoardDbModel.TABLE_NAME.value())
            .append("  WHERE id = '")
            .append(boardId)
            .append("'");
        return getData(dbRead.rawQuery(query.toString(), null)).get(0);
    }

    /**
     * @param category of board.
     * @return selected boards by category.
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
     * @param category of boards.
     * @param parentId of parent board.
     * @return boards that are first level children of parent board.
     */
    public List<Long> getBoards(String category, Long parentId)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(BoardDbModel.TABLE_NAME.value())
            .append("  WHERE type = '")
            .append(category)
            .append("' AND ")
            .append(BoardDbModel.PARENT.value())
            .append(" = ")
            .append(parentId);
        return getIds(dbRead.rawQuery(query.toString(), null));
    }

    private List<Long> getIds(Cursor query){
        List<Long> result = new ArrayList<>();
        while (query.moveToNext())
        {
            result.add(query.getLong(query.getColumnIndex(BoardDbModel.ID.value())));
        }
        return result;
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
                    if (attr.equals(BoardDbModel.ID.value()) || attr.equals(BoardDbModel.PARENT.value()))
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
}
