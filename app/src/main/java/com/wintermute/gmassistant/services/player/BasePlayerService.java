package com.wintermute.gmassistant.services.player;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import com.wintermute.gmassistant.GmAssistant;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.database.dao.LightDao;
import com.wintermute.gmassistant.database.dao.SceneDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.model.Light;
import com.wintermute.gmassistant.handlers.LightHandler;

import java.math.BigDecimal;

public class BasePlayerService extends Service
{
    String sceneId;
    String trackId;
    String playlistId;

    /**
     * Changes the light.
     *
     * @param sceneId
     */
    void changeLight(String sceneId)
    {
        SceneDao sceneDao = new SceneDao(this);
        String lightId = sceneDao.getById(sceneId).getLight();
        if (!"null".equals(lightId) && null != lightId)
        {
            LightDao dao = new LightDao(getBaseContext());
            Light light = dao.getById(lightId);
            Color color = Color.valueOf(new BigDecimal(String.valueOf(light.getColor())).intValue());
            LightHandler handler = new LightHandler(getBaseContext(), color, Integer.parseInt(light.getBrightness()));
            handler.setLight(false);
        }
    }

    /**
     * Configures the media player.
     *
     * @param intent to get extras from.
     */
    void getExtras(Intent intent)
    {
        sceneId = intent.getStringExtra("sceneId");
        playlistId = intent.getStringExtra("playlistId");
        trackId = intent.getStringExtra("trackId");
    }

    /**
     * @return next track to play.
     */
    String getMusic(String sceneId)
    {
        SceneDao dao = new SceneDao(getBaseContext());
        return dao.getById(sceneId).getBackgroundMusic();
    }

    String getAmbience(String sceneId)
    {
        SceneDao dao = new SceneDao(getBaseContext());
        return dao.getById(sceneId).getBackgroundAmbience();
    }

    /**
     * @param trackId to extract the path.
     * @return path of track
     */
    String getTrackPath(String trackId)
    {
        TrackDao dao = new TrackDao(getBaseContext());
        return dao.computeTrackIfAbsent(trackId).getPath();
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
