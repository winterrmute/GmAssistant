package com.wintermute.gmassistant.services.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.wintermute.gmassistant.GmAssistant;
import com.wintermute.gmassistant.R;

public class BasePlayer extends Service
{

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    /**
     * Creates notification for started service.
     *
     * @param intent
     * @param name
     * @param channelId
     * @param receiver
     * @return
     */
    Notification createNotification(Intent intent, String name, String channelId, Class receiver)
    {
        String input = intent.getStringExtra("inputExtra");
        createNotificationChannel(name, channelId);
        Intent notificationIntent = new Intent(this, GmAssistant.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent broadcastIntent = new Intent(this, receiver);
        PendingIntent actionIntent =
            PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, channelId)
            .setContentTitle(name)
            .setContentText(input)
            .setSmallIcon(R.drawable.play)
            .setAutoCancel(false)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.end, "Stop", actionIntent)
            .build();
    }

    /**
     * Configures notification channel.
     *
     * @param name
     * @param channelId
     */
    void createNotificationChannel(String name, String channelId)
    {
        NotificationChannel serviceChannel =
            new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_LOW);

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null)
        {
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
