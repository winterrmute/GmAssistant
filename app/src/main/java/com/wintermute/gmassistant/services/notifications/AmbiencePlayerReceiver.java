package com.wintermute.gmassistant.services.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.wintermute.gmassistant.services.player.AmbiencePlayerService;
import com.wintermute.gmassistant.services.player.MusicPlayerService;

public class AmbiencePlayerReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context ctx, Intent intent) {
        ctx.stopService(new Intent(ctx, AmbiencePlayerService.class));
    }

}
