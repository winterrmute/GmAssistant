package com.wintermute.gmassistant.client.view.effects;

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
import com.wintermute.gmassistant.adapters.EffectGroupsAdapter;
import com.wintermute.gmassistant.client.StorageBrowser;
import com.wintermute.gmassistant.dialogs.ListDialog;
import com.wintermute.gmassistant.model.Board;
import com.wintermute.gmassistant.operations.EffectBoardOperations;
import com.wintermute.gmassistant.operations.TrackOperations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * List containing effect boards.
 */
public class EffectBoards extends AppCompatActivity
{

    public static final int COLLECT_TRACKS = 0;
    public static final int DELETE = 2;
    private Board board;
    private List<Board> boards;
    private ListView effectBoards;
    private String groupName;
    private ArrayList<String> collectedTracks;
    private EffectBoardOperations operations;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect_groups);

        operations = new EffectBoardOperations(getApplicationContext());
        effectBoards = findViewById(R.id.effect_groups);
        showBoards();

        effectBoards.setOnItemClickListener((parent, view, position, id) -> openBoard(boards.get(position).getId()));
        effectBoards.setOnItemLongClickListener((parent, view, position, id) ->
        {
            board = boards.get(position);
            Intent dialog = new Intent(EffectBoards.this, ListDialog.class);
            dialog.putStringArrayListExtra("opts", new ArrayList<>(Collections.singletonList("delete")));
            dialog.putExtra("lol", "also theoretisch...");
            startActivityForResult(dialog, DELETE);
            return true;
        });

        Button addEffectGroup = findViewById(R.id.add_effect_group);
        addEffectGroup.setOnClickListener(
            v -> startActivityForResult(new Intent(this, StorageBrowser.class), COLLECT_TRACKS));
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
                }
            }
            if (requestCode == COLLECT_TRACKS)
            {
                Toast
                    .makeText(getApplicationContext(),
                        "If you selected a large directory, please wait a moment. It could take a while...",
                        Toast.LENGTH_LONG)
                    .show();
                collectedTracks = new ArrayList<>();
                collectedTracks = data.getStringArrayListExtra("effects");
                if (collectedTracks != null && collectedTracks.size() > 0)
                {
                    setGroupName();
                }
            }
        }
    }

    private void setGroupName()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Group Name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.ok_result, (dialog, which) ->
        {
            dialog.dismiss();
            groupName = input.getText().toString();
            if (!"".equals(groupName))
            {
                createGroup(collectedTracks);
                finish();
            } else
            {
                Toast.makeText(getApplicationContext(), "Set group name", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel_result, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void createGroup(List<String> collected)
    {
        TrackOperations trackOperations = new TrackOperations(getApplicationContext());
        List<Long> referencedEffects = collected
            .stream()
            .map(p -> trackOperations.storeTrackIfNotExist(trackOperations.createTrack(p)))
            .collect(Collectors.toList());

        operations.createBoard(groupName, referencedEffects);
    }

    private void openBoard(Long id)
    {
        Intent board = new Intent(this, com.wintermute.gmassistant.client.view.effects.EffectBoard.class);
        board.putExtra("groupId", id);
        startActivity(board);
    }

    private void showBoards()
    {
        EffectBoardOperations operations = new EffectBoardOperations(getApplicationContext());
        boards = operations.loadViewElements();
        EffectGroupsAdapter adapter = new EffectGroupsAdapter(this, boards);
        effectBoards = findViewById(R.id.effect_groups);
        effectBoards.setAdapter(adapter);
    }
}
