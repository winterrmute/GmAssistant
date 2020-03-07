package com.wintermute.gmassistant.client.view.effects;

import android.content.ContentValues;
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
import com.wintermute.gmassistant.database.dao.EffectsDao;
import com.wintermute.gmassistant.database.dao.GroupDao;
import com.wintermute.gmassistant.database.model.EffectDbModel;
import com.wintermute.gmassistant.database.model.GroupDbModel;
import com.wintermute.gmassistant.model.EffectBoard;
import com.wintermute.gmassistant.operations.EffectBoardOperations;
import com.wintermute.gmassistant.operations.TrackOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EffectGroups extends AppCompatActivity
{

    private List<EffectBoard> boards;
    private ListView effectBoards;
    private EffectBoardOperations operations;
    private String groupName;
    private List<Long> trackIds;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_effect_groups);

        effectBoards = findViewById(R.id.effect_groups);
        showBoards();

        effectBoards.setOnItemClickListener((parent, view, position, id) -> openBoard(boards.get(position).getId()));

        Button addEffectGroup = findViewById(R.id.add_effect_group);
        addEffectGroup.setOnClickListener(v ->  {
            startActivityForResult(new Intent(this, StorageBrowser.class), 0);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            ArrayList<String> collected = data.getStringArrayListExtra("effects");
            if (collected != null){
                prepareGroupCreation(collected);
            }
        }
    }

    private void prepareGroupCreation(List<String> collected)
    {
        trackIds = new ArrayList<>();
        TrackOperations operations = new TrackOperations(getApplicationContext());
        trackIds = collected
            .stream()
            .map(p -> operations.storeTrackIfNotExist(operations.createTrack(p)))
            .collect(Collectors.toList());
        setGroupName();
    }

    private void createGroup(){
        ContentValues values = new ContentValues();
        values.put(GroupDbModel.NAME.value(), groupName);
        GroupDao dao = new GroupDao(getApplicationContext());
        Long groupId = dao.createGroup(values);

        EffectsDao effectsDao = new EffectsDao(getApplicationContext());
        for (Long trackId : trackIds) {
            values = new ContentValues();
            values.put(EffectDbModel.TRACK_ID.value(), trackId);
            values.put(EffectDbModel.GROUP_ID.value(), groupId);
            effectsDao.addToGroup(values);
        }
    }

    private void setGroupName()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Group Name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(R.string.ok_result, (dialog, which) -> {
            groupName = input.getText().toString();
            if (!"".equals(groupName)) {
                createGroup();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Set group name", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.cancel_result, (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void openBoard(Long id)
    {
        Intent board = new Intent(this, com.wintermute.gmassistant.client.view.effects.EffectBoard.class);
        board.putExtra("groupId", id);
        startActivity(board);
    }

    private void showBoards()
    {
        operations = new EffectBoardOperations(getApplicationContext());
        boards = operations.loadViewElements();
        EffectGroupsAdapter adapter = new EffectGroupsAdapter(this, boards);
        effectBoards = findViewById(R.id.effect_groups);
        effectBoards.setAdapter(adapter);
    }
}
