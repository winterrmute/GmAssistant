package com.wintermute.gmassistant.operations;

import android.content.Context;
import com.wintermute.gmassistant.database.dao.EffectsDao;
import com.wintermute.gmassistant.database.dao.GroupDao;
import com.wintermute.gmassistant.model.EffectBoard;

import java.util.ArrayList;
import java.util.List;

public class EffectBoardOperations
{
    private Context ctx;

    public EffectBoardOperations(Context ctx)
    {
        this.ctx = ctx;
    }

    public List<EffectBoard> loadViewElements()
    {
        EffectsDao dao = new EffectsDao(ctx);
        return getBoards(dao.getBoards());
    }

    private List<EffectBoard> getBoards(List<Long> boardIds)
    {
        List<EffectBoard> result = new ArrayList<>();
        GroupDao groupDao = new GroupDao(ctx);
        for (Long boardId : boardIds)
        {
            String name = groupDao.get(boardId).get(boardId);
            result.add(new EffectBoard(boardId, name));
        }
        return result;
    }
}
