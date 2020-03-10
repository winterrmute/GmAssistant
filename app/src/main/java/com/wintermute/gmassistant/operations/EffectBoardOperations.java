package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.EffectsDao;
import com.wintermute.gmassistant.database.dao.GroupDao;
import com.wintermute.gmassistant.database.model.EffectDbModel;
import com.wintermute.gmassistant.database.model.GroupDbModel;
import com.wintermute.gmassistant.view.model.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages handling effect boards.
 *
 * @author wintermute
 */
public class EffectBoardOperations
{
    private Context ctx;

    /**
     * Creates an instance
     *
     * @param ctx application context
     */
    public EffectBoardOperations(Context ctx)
    {
        this.ctx = ctx;
    }

    /**
     * Creates group with referenced effects
     *
     * @param boardName containing referenced effects
     * @param effects on given board
     */
    public void createBoard(String boardName, List<Long> effects)
    {
        ContentValues values = new ContentValues();
        values.put(GroupDbModel.NAME.value(), boardName);

        GroupDao dao = new GroupDao(ctx);
        referenceEffectsToBoard(dao.create(values), effects);
    }

    private void referenceEffectsToBoard(Long groupId, List<Long> boardEffects)
    {
        EffectsDao effectsDao = new EffectsDao(ctx);
        for (Long trackId : boardEffects)
        {
            ContentValues values = new ContentValues();
            values.put(EffectDbModel.TRACK_ID.value(), trackId);
            values.put(EffectDbModel.GROUP_ID.value(), groupId);
            effectsDao.addToGroup(values);
        }
    }

    public void deleteBoard(Board board)
    {
        GroupDao dao = new GroupDao(ctx);
        dao.delete(board);
    }

    /**
     * @return list of all effect boards.
     */
    public List<Board> loadViewElements()
    {
        EffectsDao dao = new EffectsDao(ctx);
        return getBoards(dao.getBoards());
    }

    private List<Board> getBoards(List<Long> boardIds)
    {
        List<Board> result = new ArrayList<>();
        GroupDao groupDao = new GroupDao(ctx);
        for (Long boardId : boardIds)
        {
            String name = groupDao.get(boardId).get(boardId);
            result.add(new Board(boardId, name));
        }
        return result;
    }
}
