package com.wintermute.gmassistant.services.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.services.player.MusicPlayer;

public class MusicReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context ctx, Intent intent)
    {
        PlayerOperations.getInstance().stopPlayer("music");
        ctx.stopService(new Intent(ctx, MusicPlayer.class));
    }
}
