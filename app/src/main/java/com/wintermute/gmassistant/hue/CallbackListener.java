package com.wintermute.gmassistant.hue;

import org.json.JSONArray;
import org.json.JSONObject;

interface CallbackListener
{
    void onResponse(JSONArray response);

    void onResponse(JSONObject response);

    void onError(String msg);
}
