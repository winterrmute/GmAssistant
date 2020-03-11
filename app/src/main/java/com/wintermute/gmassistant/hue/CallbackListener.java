package com.wintermute.gmassistant.hue;

import org.json.JSONArray;
import org.json.JSONObject;

public interface CallbackListener
{
    void onResponse(JSONArray response);

    void onResponse(JSONObject response);

    void onError(String msg);
}
