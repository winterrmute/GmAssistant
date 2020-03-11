package com.wintermute.gmassistant.services;

import android.content.Context;
import com.wintermute.gmassistant.hue.model.HueBridge;
import com.wintermute.gmassistant.hue.model.HueBulb;
import com.wintermute.gmassistant.operations.LightConfigOperations;

import java.util.List;

public class LightConnection
{
    private static LightConnection instance;
    private HueBridge bridge;
    private List<HueBulb> bulbs;

    public static LightConnection getInstance(){
        if (instance == null){
            return new LightConnection();
        }
        return instance;
    }

    public void init(Context ctx, HueBridge bridge){
        this.bridge = bridge;

        LightConfigOperations operations = new LightConfigOperations(ctx);
        bulbs = operations.getConnectedBulbs(bridge);
        System.out.println();
    }
}
