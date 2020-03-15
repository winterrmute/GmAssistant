package com.wintermute.gmassistant.database.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.wintermute.gmassistant.database.DbManager;
import com.wintermute.gmassistant.database.model.EffectDbModel;

import java.util.ArrayList;
import java.util.List;

public class EffectsDao
{
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    public EffectsDao(Context ctx)
    {
        DbManager dbManager = new DbManager(ctx);
        dbRead = dbManager.getReadableDatabase();
        dbWrite = dbManager.getWritableDatabase();
        dbWrite.execSQL("PRAGMA foreign_keys=ON;");
    }

    /**
     * Get track details.
     *
     * @param boardId of selected board.
     * @return selected trackIds assigned to this board.
     */
    public List<Long> get(Long boardId)
    {
        StringBuilder query = new StringBuilder("SELECT ")
            .append(EffectDbModel.TRACK_ID.value())
            .append(" FROM ")
            .append(EffectDbModel.TABLE_NAME.value())
            .append("  WHERE ")
            .append(EffectDbModel.BOARD_ID.value())
            .append(" = '")
            .append(boardId)
            .append("'");
        return getBoardData(dbRead.rawQuery(query.toString(), null), EffectDbModel.TRACK_ID.value());
    }

    private List<Long> getBoardData(Cursor query, String attribute)
    {
        List<Long> groups = new ArrayList<>();
        while (query.moveToNext())
        {
            groups.add(query.getLong(query.getColumnIndex(attribute)));
        }
        return groups;
    }

    public Long assignToBoard(ContentValues values)
    {
        return dbWrite.insert(EffectDbModel.TABLE_NAME.value(), null, values);
    }
}
