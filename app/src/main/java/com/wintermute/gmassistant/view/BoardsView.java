package com.wintermute.gmassistant.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.adapters.BoardsAdapter;
import com.wintermute.gmassistant.dialogs.ListDialog;
import com.wintermute.gmassistant.operations.BoardOperations;
import com.wintermute.gmassistant.view.effects.EffectBoard;
import com.wintermute.gmassistant.view.model.Board;
import com.wintermute.gmassistant.view.scenes.SceneBoard;

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
    private ListView boardsView;
    private String boardName;
    private BoardOperations operations;
    private BoardsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect_groups);

        operations = new BoardOperations(getApplicationContext());
        boardsView = findViewById(R.id.boards);
        showBoards();

        boardsView.setOnItemClickListener((parent, view, position, id) -> {
            board = boards.get(position);
            openBoard(boards.get(position));
        });
        boardsView.setOnItemLongClickListener((parent, view, position, id) ->
        {
            board = boards.get(position);
            Intent dialog = new Intent(BoardsView.this, ListDialog.class);
            dialog.putStringArrayListExtra("opts", new ArrayList<>(Collections.singletonList("delete")));
            startActivityForResult(dialog, DELETE);
            return true;
        });

        Button addBoard = findViewById(R.id.add_board);
        addBoard.setOnClickListener(v -> createBoard());
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
                    updateBoardsView(getIntent().getLongExtra("boardId", -1L));
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
                Long parentId = board != null ? board.getId() : null;
                Board newBoard =
                    operations.createBoard(boardName, getIntent().getStringExtra("boardCategory"), parentId, null);
                openBoard(newBoard);
                updateBoardsView(getIntent().getLongExtra("boardId", -1L));
            } else
            {
                Toast.makeText(getApplicationContext(), "Set Board name", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel_result, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateBoardsView(Long parentId)
    {
        operations = operations == null ? new BoardOperations(getApplicationContext()) : operations;
        boards = operations.loadBoardsByParent(getIntent().getStringExtra("boardCategory"), parentId);
        adapter = adapter == null ? new BoardsAdapter(this, boards) : adapter;
        adapter.updateDisplayedElements(boards);
    }

    private void openBoard(Board board)
    {
        board = operations.getBoard(board.getId());
        if (!board.isHasChildren())
        {
            Class boardCategory = null;
            if ("scenes".equals(getIntent().getStringExtra("boardCategory")))
            {
                boardCategory = SceneBoard.class;
            } else if ("effects".equals(getIntent().getStringExtra("boardCategory")))
            {
                boardCategory = EffectBoard.class;
            }

            Intent intent = new Intent(this, boardCategory);
            intent.putExtra("boardId", board.getId());
            startActivity(intent);
        } else {
            updateBoardsView(board.getId());
        }
    }

    private void showBoards()
    {
        updateBoardsView(getIntent().getLongExtra("boardId", -1L));
        boardsView = findViewById(R.id.boards);
        boardsView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed(){
        if (board.getParent() != -1L) {
            board = operations.getBoard(board.getParent());
            updateBoardsView(board.getParent());
        } else {
            finish();
        }
    }
}
