package com.wintermute.gmassistant.services.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.wintermute.gmassistant.GmAssistant;
import com.wintermute.gmassistant.R;

import java.util.Random;

/**
 * Audio player for playing audio files by any tags.
 */
public class AudioPlayer extends Service
{

    private static final String TAG = "AudioPlayer";
    private MediaPlayer player;

    public IBinder onBind(Intent arg0)
    {
        Log.i(TAG, "onBind()");
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        player = new MediaPlayer();
        player.setLooping(true); // Set looping
        player.setVolume(1f, 1f);
    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        //just temporary id to keep the notifications separated
        int id = new Random().nextInt(50 + 1);
        Notification notification =
            createNotification(String.valueOf(id), String.valueOf(id));
        startForeground(id, notification);
        player = MediaPlayer.create(this, Uri.parse("file://" + intent.getStringExtra("path")));
        player.start();
        return Service.START_STICKY;
    }

    public IBinder onUnBind(Intent arg0)
    {
        Log.i(TAG, "onUnBind()");
        return null;
    }

    public void onStop()
    {
        Log.i(TAG, "onStop()");
    }

    public void onPause()
    {
        Log.i(TAG, "onPause()");
    }

    @Override
    public void onDestroy()
    {
        player.stop();
        player.release();
    }

    Notification createNotification(String name, String channelId)
    {
        createNotificationChannel(name, channelId);
        Intent notificationIntent = new Intent(this, GmAssistant.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Intent broadcastIntent = new Intent(this, AudioPlayer.class);
        PendingIntent actionIntent =
            PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, channelId)
            .setContentTitle(name)
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
            new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT);

        NotificationManager manager = getSystemService(NotificationManager.class);
        if (manager != null)
        {
            manager.createNotificationChannel(serviceChannel);
        }
    }
}