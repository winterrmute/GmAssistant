package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.BulbDao;
import com.wintermute.gmassistant.database.dao.HueBridgeDao;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.hue.model.HueBulb;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LightConfigOperations
{
    private final Context ctx;
    private HueBridgeDao dao;

    public LightConfigOperations(Context ctx)
    {
        this.ctx = ctx;
        dao = new HueBridgeDao(ctx);
    }

    public List<HueBridge> getBridges()
    {
        List<HueBridge> result = new ArrayList<>();
        List<Map<String, String>> bridges = dao.getAll();
        for (Map<String, String> bridgeDetails : bridges)
        {
            result.add(new HueBridge(bridgeDetails.get("name"), bridgeDetails.get("ip"), bridgeDetails.get("username")));
        }
        return result;
    }

    public void storeConfig(String name, String ip, String username)
    {
        ContentValues values = new ContentValues();
        values.put("name", name);
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

    public void delete(HueBridge bridge)
    {
        dao.delete(bridge);
    }
}
