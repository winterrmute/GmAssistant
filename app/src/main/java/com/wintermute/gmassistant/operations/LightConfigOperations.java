package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import com.wintermute.gmassistant.database.dao.LightConfigDao;

public class LightConfigOperations
{
    private final Context ctx;

    public LightConfigOperations(Context ctx){
        this.ctx = ctx;
    }

    public void getBridge(String ip)
    {
        LightConfigDao dao = new LightConfigDao(ctx);
        dao.get(ip);
    }

    public void storeConfig(String ip, String username){
        LightConfigDao dao = new LightConfigDao(ctx);
        ContentValues values = new ContentValues();
        values.put("ip", ip);
        values.put("username", username);
        dao.insert(values);
    }
}
