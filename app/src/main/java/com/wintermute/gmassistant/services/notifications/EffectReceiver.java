package com.wintermute.gmassistant.services.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.wintermute.gmassistant.operations.PlayerOperations;
import com.wintermute.gmassistant.services.player.EffectPlayer;

public class EffectReceiver extends BroadcastReceiver
{
    public void onReceive(Context ctx, Intent intent)
    {
        PlayerOperations.getInstance().stopPlayer("effect");
        ctx.stopService(new Intent(ctx, EffectPlayer.class));
    }
}
