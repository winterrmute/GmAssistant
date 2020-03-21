package com.wintermute.gmassistant.view.boards;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.BoardsAdapter;
import com.wintermute.gmassistant.database.dao.EffectsDao;
import com.wintermute.gmassistant.dialogs.ListDialog;
import com.wintermute.gmassistant.operations.BoardOperations;
import com.wintermute.gmassistant.operations.SceneOperations;
import com.wintermute.gmassistant.view.boards.scenes.SceneBoard;
import com.wintermute.gmassistant.view.boards.scenes.SceneConfig;
import com.wintermute.gmassistant.view.model.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * List containing effect boards.
 */
public class BoardsView extends AppCompatActivity
{

    public static final int DELETE = 2;
    private Board board;
    private List<Board> boards;
    private String boardName;
    private BoardOperations operations;
    private BoardsAdapter adapter;
    private String category;
    private Long currentBoardId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boards);

        init();
        handleBackFromCategoryBoard();

        Button addBoard = findViewById(R.id.add_board);
        addBoard.setOnClickListener(v -> createBoard());
    }

    private void handleBackFromCategoryBoard()
    {
        if (getIntent().getLongExtra("boardId", -1L) != -1L)
        {
            board = operations.getBoard(getIntent().getLongExtra("boardId", -1L));
            category = board.getType();
            if (operations.getParent(board) != null)
            {
                board = operations.getParent(board);
                updateBoardsView(board.getId());
            } else
            {
                board = null;
                updateBoardsView(null);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == DELETE)
            {
                String action = data.getStringExtra("action");
                if ("delete".equals(action))
                {
                    operations.deleteBoard(board);
                    updateBoardsView(currentBoardId);
                }
            }
        }
    }

    private void createBoard()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Board Name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.ok_result, (dialog, which) ->
        {
            dialog.dismiss();
            boardName = input.getText().toString();
            if (!"".equals(boardName))
            {
                if (board != null)
                {
                    operations.setParentFlag(board.getId());
                    board = operations.createNestedBoard(boardName, board.getType(), false, board.getId());
                } else
                {
                    board = operations.createBoard(boardName, category, false, true);
                    if (isRoot(board))
                    {
                        operations.setTopLevelFlag(board.getId());
                    }
                }
                openBoard();
            } else
            {
                Toast.makeText(getApplicationContext(), "Set Board name", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel_result, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private boolean isRoot(Board board)
    {
        Board parent = operations.getParent(board);
        return parent == null;
    }

    private void updateBoardsView(Long parentId)
    {
        operations = operations == null ? new BoardOperations(getApplicationContext()) : operations;
        if (board == null)
        {
            boards = operations.getTopLevelBoards(category);
        } else
        {
            boards = operations.getNestedBoards(category, parentId);
        }
        configureViewDependingOnContent();
        adapter.updateDisplayedElements(boards);
    }

    private void configureViewDependingOnContent()
    {
        if (boards.size() > 0)
        {
            findViewById(R.id.add_content).setVisibility(View.GONE);
            findViewById(R.id.boards).setVisibility(View.VISIBLE);
        } else
        {
            initEmptyContentBoardView();
        }
    }

    private void initBoardList()
    {
        boards = new ArrayList<>();
        adapter = new BoardsAdapter(this, boards);

        ListView boardsView = findViewById(R.id.boards);
        boardsView.setAdapter(adapter);
        boardsView.setOnItemClickListener((parent, view, position, id) ->
        {
            board = boards.get(position);
            openBoard();
        });
        boardsView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            board = boards.get(position);
            Intent dialog = new Intent(BoardsView.this, ListDialog.class);
            dialog.putStringArrayListExtra("opts", new ArrayList<>(Collections.singletonList("delete")));
            startActivityForResult(dialog, DELETE);
            return true;
        });
    }

    private void openBoard()
    {
        if (hasElements(board))
        {
            Class boardCategory = null;
            if ("scenes".equals(category))
            {
                boardCategory = SceneBoard.class;
            } else if ("effects".equals(category))
            {
                boardCategory = EffectBoard.class;
            }
            Intent intent = new Intent(getApplicationContext(), boardCategory);
            intent.putExtra("boardId", board.getId());
            startActivity(intent);
            finish();
        } else
        {
            updateBoardsView(board.getId());
        }
    }

    private void initEmptyContentBoardView()
    {
        Button addContent = findViewById(R.id.add_content);
        addContent.setVisibility(View.VISIBLE);
        if (board == null)
        {
            findViewById(R.id.add_content).setVisibility(View.GONE);
        }
        if ("scenes".equals(category))
        {
            addContent.setText(R.string.add_scene);
            addContent.setOnClickListener(v ->
            {
                Intent intent = new Intent(getApplicationContext(), SceneConfig.class);
                intent.putExtra("boardId", board.getId());
                startActivity(intent);
            });
        } else
        {
            addContent.setText(R.string.add_effects);
            addContent.setOnClickListener(v ->
            {
                Intent intent = new Intent(getApplicationContext(), EffectBoard.class);
                intent.putExtra("newEffectBoard", true);
                intent.putExtra("boardId", board.getId());
                startActivity(intent);
            });
        }
    }

    private boolean hasElements(Board board)
    {
        if (board.getType().equals("scenes"))
        {
            SceneOperations operations = new SceneOperations(getApplicationContext());
            return operations.getScenesAssignedToBoard(board.getId()).size() > 0;
        } else
        {
            EffectsDao dao = new EffectsDao(getApplicationContext());
            return dao.get(board.getId()).size() > 0;
        }
    }

    private void init()
    {
        operations = new BoardOperations(getApplicationContext());
        initBoardList();
        category = getIntent().getStringExtra("boardCategory");
        currentBoardId = getIntent().getLongExtra("boardId", -1L);
        updateBoardsView(currentBoardId);
    }

    @Override
    public void onBackPressed()
    {
        if (board != null)
        {
            Board parent = operations.getParent(board);
            if (parent != null)
            {
                updateBoardsView(parent.getId());
                board = parent;
            } else
            {
                board = null;
                updateBoardsView(null);
            }
        } else
        {
            finish();
        }
    }
}
