package com.wintermute.gmassistant.services;

import android.content.Context;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.hue.model.HueBulb;
import com.wintermute.gmassistant.operations.LightConfigOperations;

import java.util.ArrayList;
import java.util.List;

/**
 * Establishes connection to philips hue bridge and holds the information.
 */
public class LightConnection
{
    private HueBridge bridge;
    private List<HueBulb> bulbs;

    private LightConnection() {}

    /**
     * @return an ApiCaller instance exactly when it´s required.
     */
    public static LightConnection getInstance()
    {
        return LightConnection.LayInit.instance;
    }

    public void init(Context ctx, HueBridge bridge)
    {
        this.bridge = bridge;
        LightConfigOperations operations = new LightConfigOperations(ctx);
        bulbs = operations.getConnectedBulbs(bridge);
    }

    private String getLightManagementUrl()
    {
        return "http://" + bridge.getIp() + "/api/" + bridge.getUsername() + "/lights";
    }

    public List<String> getBulbs()
    {
        List<String> result = new ArrayList<>();
        if (bulbs != null)
        {
            for (HueBulb bulb : bulbs)
            {
                result.add(getLightManagementUrl() + "/" + bulb.getId().toString() + "/state");
            }
        }
        return result;
    }

    private static class LayInit
    {
        private static final LightConnection instance = new LightConnection();
    }
}
