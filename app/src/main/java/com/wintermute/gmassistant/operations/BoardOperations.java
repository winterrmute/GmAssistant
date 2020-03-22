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
import java.util.stream.Collectors;

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
    public Board createBoard(String name, String type, Boolean isParent, Boolean isRoot)
    {
        isParent = isParent == null ? false : isParent;
        ContentValues values = new ContentValues();
        values.put(BoardDbModel.NAME.value(), name);
        values.put(BoardDbModel.TYPE.value(), type);
        values.put(BoardDbModel.IS_PARENT.value(), String.valueOf(isParent));
        values.put(BoardDbModel.IS_ROOT.value(), String.valueOf(isRoot));
        return getBoard(dao.insert(values));
    }

    public Board createNestedBoard(String name, String type, Boolean isParent, Long parentId)
    {
        Board board = createBoard(name, type, isParent, false);
        ContentValues values = new ContentValues();
        values.put("boardId", board.getId());
        values.put("parentId", parentId);
        dao.createNestedBoard(values);
        return board;
    }

    /**
     * @param boardId to get from database.
     * @return requested board.
     */
    public Board getBoard(Long boardId)
    {
        Map<String, Object> boardData = dao.get(boardId);
        return new Board((Long) boardData.get(BoardDbModel.ID.value()),
            (String) boardData.get(BoardDbModel.NAME.value()), (String) boardData.get(BoardDbModel.TYPE.value()),
            Boolean.parseBoolean((String) boardData.get(BoardDbModel.IS_PARENT.value())), Boolean.parseBoolean((String) boardData.get(BoardDbModel.IS_ROOT.value())));
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
    public List<Board> getNestedBoards(String category, Long parentId)
    {
        List<Board> result = new ArrayList<>();
        List<Long> received = dao.getBoards(category, parentId);
        received.forEach(id -> result.add(getBoard(id)));
        return result;
    }

    public Board getParent(Board board)
    {
        Long parentId = dao.getParentId(board);
        return parentId != null ? getBoard(parentId) : null;
    }

    public List<Board> getTopLevelBoards(String category)
    {
        List<Map<String, Object>> rootBoards = dao.getRootBoards(category);
        return rootBoards.stream().map(e -> getBoard((Long) e.get("id"))).collect(Collectors.toList());
    }

    /**
     * Make board to parent board
     *
     * @param boardId to update board.
     */
    public void setParentFlag(Long boardId)
    {
        update(BoardDbModel.IS_PARENT.value(), boardId);
    }

    /**
     * Make board to top level board
     *
     * @param boardId to update board.
     */
    public void setTopLevelFlag(Long boardId)
    {
        update(BoardDbModel.IS_ROOT.value(), boardId);
    }

    private void update(String attr, Long boardId){
        ContentValues values = new ContentValues();
        values.put(attr, "true");
        dao.update(values, boardId);
    }

    public void addLightToBoard(Long boardId, Long lightId){
        ContentValues values = new ContentValues();
        values.put("lightEffect", lightId);
        dao.update(values, boardId);
    }

    public Long getLight(Long boardId)
    {
        return dao.getLightForBoard(boardId);
    }
}
