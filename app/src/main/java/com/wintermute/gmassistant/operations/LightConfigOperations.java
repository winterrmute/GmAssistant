package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.BulbDao;
import com.wintermute.gmassistant.database.dao.HueBridgeDao;
import com.wintermute.gmassistant.hue.model.HueBulb;
import com.wintermute.gmassistant.hue.model.HueUser;

import java.util.Map;

public class LightConfigOperations
{
    private final Context ctx;

    public LightConfigOperations(Context ctx)
    {
        this.ctx = ctx;
    }

    public HueUser getBridge()
    {
        HueBridgeDao dao = new HueBridgeDao(ctx);
        Map<String, String> connectionData = dao.get();
        if (connectionData.get("ip") == null) {
            return null;
        }
        return new HueUser(connectionData.get("ip"), connectionData.get("username"));
    }

    public void storeConfig(String ip, String username)
    {
        HueBridgeDao dao = new HueBridgeDao(ctx);
        ContentValues values = new ContentValues();
        values.put("ip", ip);
        values.put("username", username);
        dao.insert(values);
    }

    public void addBulbs(Map<String, HueBulb> bulbs)
    {
        BulbDao dao = new BulbDao(ctx);
        ContentValues values;
        for (HueBulb bulb : bulbs.values())
        {
            values = new ContentValues();
            values.put("name", bulb.getName());
            values.put("type", bulb.getType());
            dao.insert(values);
        }
    }
}
