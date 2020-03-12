package com.wintermute.gmassistant.services;

import android.content.Context;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.hue.model.HueBulb;
import com.wintermute.gmassistant.operations.LightConfigOperations;

import java.util.ArrayList;
import java.util.List;

public class LightConnection
{
    private static LightConnection instance;
    private HueBridge bridge;
    private List<HueBulb> bulbs;

    public static LightConnection getInstance()
    {
        if (instance == null)
        {
            instance = new LightConnection();
            return instance;
        }
        return instance;
    }

    public void init(Context ctx, HueBridge bridge)
    {
        this.bridge = bridge;

        LightConfigOperations operations = new LightConfigOperations(ctx);
        bulbs = operations.getConnectedBulbs(bridge);
    }

    public void kill()
    {
        instance = null;
    }

    public String getLightsUrl()
    {
        return "http://" + bridge.getIp() + "/api/" + bridge.getUsername() + "/lights";
    }

    public List<String> getLightManagementUrls(){
        List<String> result = new ArrayList<>();
        for (HueBulb bulb : bulbs) {
            result.add(getLightsUrl() + "/" + bulb.getId().toString() + "/state");
        }
        return result;
    }
}
