package com.wintermute.gmassistant.view.effects;

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
import com.wintermute.gmassistant.view.model.Board;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * List containing effect boards.
 */
public class EffectBoards extends AppCompatActivity
{

    public static final int DELETE = 2;
    private Board board;
    private List<Board> boards;
    private ListView effectBoards;
    private String boardName;
    private BoardOperations operations;
    private BoardsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect_groups);

        operations = new BoardOperations(getApplicationContext());
        effectBoards = findViewById(R.id.effect_groups);
        showBoards();

        effectBoards.setOnItemClickListener((parent, view, position, id) -> openBoard(boards.get(position).getId()));
        effectBoards.setOnItemLongClickListener((parent, view, position, id) ->
        {
            board = boards.get(position);
            Intent dialog = new Intent(EffectBoards.this, ListDialog.class);
            dialog.putStringArrayListExtra("opts", new ArrayList<>(Collections.singletonList("delete")));
            startActivityForResult(dialog, DELETE);
            return true;
        });

        Button addBoard = findViewById(R.id.add_effect_group);
        addBoard.setOnClickListener(v -> createBoard());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            if (requestCode == 2)
            {
                String action = data.getStringExtra("action");
                if ("delete".equals(action))
                {
                    operations.deleteBoard(board);
                    updateBoards();
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
                Long createdBoardId = operations.createBoard(boardName, "effects");
                openBoard(createdBoardId);
                updateBoards();
            } else
            {
                Toast.makeText(getApplicationContext(), "Set Board name", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel_result, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateBoards()
    {
        boards = operations.loadViewElements("effects");
        adapter.updateDisplayedElements(boards);
    }

    private void openBoard(Long id)
    {
        Intent board = new Intent(this, EffectBoard.class);
        board.putExtra("boardId", id);
        startActivity(board);
    }

    private void showBoards()
    {
        operations = new BoardOperations(getApplicationContext());
        boards = operations.loadViewElements("effects");
        adapter = new BoardsAdapter(this, boards);
        effectBoards = findViewById(R.id.effect_groups);
        effectBoards.setAdapter(adapter);
    }
}
