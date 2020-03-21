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
     * @param values board model data.
     * @return id of created board.
     */
    public Long insert(ContentValues values)
    {
        return dbWrite.insert(BoardDbModel.TABLE_NAME.value(), null, values);
    }

    /**
     * Create entry for board in nested_board table. That means, that this board is not placed on top level.
     *
     * @param values board id and its parent id
     */
    public void createNestedBoard(ContentValues values)
    {
        dbWrite.insert(BoardDbModel.NESTED_BOARDS_TABLE_NAME.value(), null, values);
    }

    public List<Map<String, Object>> getRootBoards(String category)
    {
        StringBuilder query = new StringBuilder("SELECT * FROM ")
            .append(BoardDbModel.TABLE_NAME.value())
            .append("  WHERE type = '")
            .append(category)
            .append("' AND isRoot = 'true'");
        return getData(dbRead.rawQuery(query.toString(), null));
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

    public Long getParentId(Board board)
    {
        StringBuilder query = new StringBuilder("SELECT parentId FROM ")
            .append(BoardDbModel.NESTED_BOARDS_TABLE_NAME.value())
            .append("  WHERE ")
            .append(BoardDbModel.NESTED_BOARD_ID.value())
            .append(" = ")
            .append(board.getId());
        Map<String, Long> data = getParentBoardData(dbRead.rawQuery(query.toString(), null));
        return data.get("id");
    }

    public List<Long> getBoards(String category, Long parentId)
    {
        StringBuilder query = new StringBuilder("SELECT id FROM ")
            .append(BoardDbModel.TABLE_NAME.value())
            .append(" INNER JOIN ")
            .append(BoardDbModel.NESTED_BOARDS_TABLE_NAME.value())
            .append(" ON ")
            .append(BoardDbModel.NESTED_BOARDS_TABLE_NAME.value())
            .append(".boardId = boards.id")
            .append(" WHERE ")
            .append(BoardDbModel.NESTED_BOARDS_TABLE_NAME.value())
            .append(".parentId =")
            .append(parentId)
            .append(" AND ")
            .append(BoardDbModel.TABLE_NAME.value())
            .append(".type = '")
            .append(category)
            .append("'");
        return getIds(dbRead.rawQuery(query.toString(), null));
    }

    private List<Long> getIds(Cursor query)
    {
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
            for (String attr : BoardDbModel.getAttrs())
            {
                if (attr.equals(BoardDbModel.ID.value()))
                {
                    board.put(attr, query.getLong(query.getColumnIndex(attr)));
                } else
                {
                    board.put(attr, query.getString(query.getColumnIndex(attr)));
                }
            }
            result.add(board);
        }
        return result;
    }

    private Map<String, Long> getParentBoardData(Cursor query)
    {
        Map<String, Long> result = new HashMap<>();
        while (query.moveToNext())
        {
            result.put("id", query.getLong(query.getColumnIndex("parentId")));
        }
        return result;
    }
}
