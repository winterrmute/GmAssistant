package com.wintermute.gmassistant.services.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.wintermute.gmassistant.services.player.AmbiencePlayer;

public class AmbienceReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context ctx, Intent intent)
    {
        ctx.stopService(new Intent(ctx, AmbiencePlayer.class));
    }
}
