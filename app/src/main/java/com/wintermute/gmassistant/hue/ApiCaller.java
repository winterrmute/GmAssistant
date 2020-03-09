package com.wintermute.gmassistant.hue;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wintermute.gmassistant.hue.model.CustomRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiCaller
{

    public static ApiCaller instance;

    public static ApiCaller getInstance()
    {
        if (instance == null)
        {
            return new ApiCaller();
        }
        return instance;
    }

    public void makeCustomCall(Context ctx, int method, String url, String body, CallbackListener listener)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        JSONObject reqBody = null;
        try
        {
            reqBody = new JSONObject(body);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        CustomRequest req = new CustomRequest(method, url, reqBody, listener::onResponse, error -> listener.onError(String.valueOf(error)));
        requestQueue.add(req);
    }

    public void makeCall(Context ctx, int method, String url, String body, CallbackListener listener)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        JSONObject reqBody = null;
        try
        {
            reqBody = new JSONObject(body);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(method, url, reqBody, listener::onResponse, error -> listener.onError(String.valueOf(error)));
        requestQueue.add(req);
    }
}
