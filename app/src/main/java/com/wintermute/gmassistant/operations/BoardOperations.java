package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.BoardDao;
import com.wintermute.gmassistant.database.dao.EffectsDao;
import com.wintermute.gmassistant.database.model.BoardDbModel;
import com.wintermute.gmassistant.database.model.EffectDbModel;
import com.wintermute.gmassistant.view.model.Board;
import com.wintermute.gmassistant.view.model.Track;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Manages handling effect boards.
 *
 * @author wintermute
 */
public class BoardOperations
{
    private Context ctx;

    /**
     * Creates an instance
     *
     * @param ctx application context
     */
    public BoardOperations(Context ctx)
    {
        this.ctx = ctx;
    }

    /**
     * Creates group with referenced effects
     *
     * @param name containing referenced effects
     * @param type of created board
     */
    public Long createBoard(String name, String type)
    {
        ContentValues values = new ContentValues();
        values.put(BoardDbModel.NAME.value(), name);
        values.put(BoardDbModel.TYPE.value(), type);

        BoardDao dao = new BoardDao(ctx);
        return dao.insert(values);
    }

    public void referenceEffectsToBoard(Long groupId, List<Track> boardEffects)
    {
        EffectsDao effectsDao = new EffectsDao(ctx);
        TrackOperations trackOperations = new TrackOperations(ctx);
        for (Track track : boardEffects)
        {
            track.setId(trackOperations.storeTrackIfNotExist(track));
            ContentValues values = new ContentValues();
            values.put(EffectDbModel.TRACK_ID.value(), track.getId());
            values.put(EffectDbModel.BOARD_ID.value(), groupId);
            effectsDao.assignToBoard(values);
        }
    }

    public void deleteBoard(Board board)
    {
        BoardDao dao = new BoardDao(ctx);
        dao.delete(board);
    }

    /**
     * @return list of all effect boards.
     */
    public List<Board> loadViewElements(String category)
    {
        List<Board> result = new ArrayList<>();
        BoardDao dao = new BoardDao(ctx);
        List<Map<String, Object>> received = dao.getCategory(category);

        received
            .stream()
            .forEach(b -> result.add(new Board((Long) b.get("id"), (String) b.get("name"), (String) b.get("typer"))));

        return result;
    }
}
