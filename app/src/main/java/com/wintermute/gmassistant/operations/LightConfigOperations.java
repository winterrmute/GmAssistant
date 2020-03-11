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
    private BulbDao bulbDao;

    public LightConfigOperations(Context ctx)
    {
        this.ctx = ctx;
        dao = new HueBridgeDao(ctx);
        bulbDao = new BulbDao(ctx);
    }

    public HueBridge getActiveBridge()
    {
        Map<String, Object> bridge = dao.getActiveBridge();
        return new HueBridge((Long) bridge.get("id"), (String) bridge.get("name"), (String) bridge.get("ip"),
            (String) bridge.get("username"), 1);
    }

    public List<HueBridge> getBridges()
    {
        List<HueBridge> result = new ArrayList<>();
        List<Map<String, Object>> bridges = dao.getAll();
        for (Map<String, Object> bridgeDetails : bridges)
        {
            result.add(new HueBridge((Long) bridgeDetails.get("id"), (String) bridgeDetails.get("name"),
                (String) bridgeDetails.get("ip"), (String) bridgeDetails.get("username"), 0));
        }
        return result.size() == 0 ? null : result;
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
        ContentValues values;
        for (HueBulb bulb : bulbs.values())
        {
            values = new ContentValues();
            values.put("name", bulb.getName());
            values.put("type", bulb.getType());
            values.put("bridgeId", bulb.getBridgeId());
            bulbDao.insert(values);
        }
    }

    public void delete(HueBridge bridge)
    {
        dao.delete(bridge);
    }

    public List<HueBulb> getConnectedBulbs(HueBridge bridge)
    {
        List<Map<String, Object>> bulbs = bulbDao.getByBridge(bridge);
        List<HueBulb> result = new ArrayList<>();
        for (Map<String, Object> bulb : bulbs)
        {
            String name = (String) bulb.get("name");
            String type = (String) bulb.get("type");
            Long bridgeId = (Long) bulb.get("bridgeId");
            result.add(new HueBulb(name, type, bridgeId));
        }
        return result;
    }
}
