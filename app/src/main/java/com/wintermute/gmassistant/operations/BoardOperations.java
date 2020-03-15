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
    private BoardDao dao;

    /**
     * Creates an instance
     *
     * @param ctx application context
     */
    public BoardOperations(Context ctx)
    {
        this.ctx = ctx;
        dao = new BoardDao(ctx);
    }

    /**
     * Creates group with referenced effects
     *
     * @param name containing referenced effects
     * @param type of created board
     */
    public Board createBoard(String name, String type, Long parent, Boolean hasChildren)
    {
        parent = parent == null ? -1L : parent;
        hasChildren = hasChildren == null ? false : hasChildren;
        ContentValues values = new ContentValues();
        values.put(BoardDbModel.NAME.value(), name);
        values.put(BoardDbModel.TYPE.value(), type);
        values.put(BoardDbModel.PARENT.value(), parent);
        values.put(BoardDbModel.HAS_CHILDREN.value(), String.valueOf(hasChildren));
        return getBoard(dao.insert(values));
    }

    /**
     * @param boardId to get from database.
     * @return requested board.
     */
    public Board getBoard(Long boardId)
    {
        Map<String, Object> boardData = dao.get(boardId);
        return new Board((Long) boardData.get(BoardDbModel.ID.value()), (String) boardData.get(BoardDbModel.NAME.value()),
            (String) boardData.get(BoardDbModel.TYPE.value()), (Long) boardData.get(BoardDbModel.PARENT.value()),
            Boolean.parseBoolean((String) boardData.get(BoardDbModel.HAS_CHILDREN.value())));
    }

    /**
     * Assign effects to created board
     *
     * @param boardId to which the effects should be assigned.
     * @param effects to assign to board id.
     */
    public void referenceEffectsToBoard(Long boardId, List<Track> effects)
    {
        EffectsDao effectsDao = new EffectsDao(ctx);
        TrackOperations trackOperations = new TrackOperations(ctx);
        for (Track track : effects)
        {
            track.setId(trackOperations.storeTrackIfNotExist(track));
            ContentValues values = new ContentValues();
            values.put(EffectDbModel.TRACK_ID.value(), track.getId());
            values.put(EffectDbModel.BOARD_ID.value(), boardId);
            effectsDao.assignToBoard(values);
        }
    }

    public void deleteBoard(Board board)
    {
        dao.delete(board);
    }

    /**
     * Loads children of selected boards if present.
     *
     * @param category of selected board.
     * @param parentId if present, else -1 (root)
     * @return children of selected board.
     */
    public List<Board> loadBoardsByParent(String category, Long parentId){
        List<Board> result = new ArrayList<>();
        List<Long> received = dao.getBoards(category, parentId);
        received.forEach(id -> result.add(getBoard(id)));
        return result;
    }

    /**
     * Sets state to parent.
     *
     * @param boardId of board in which the child has been created.
     */
    public void addChildrenToBoard(Long boardId)
    {
        ContentValues values = new ContentValues();
        values.put(BoardDbModel.HAS_CHILDREN.value(), "true");
        dao.update(values, boardId);
    }
}
