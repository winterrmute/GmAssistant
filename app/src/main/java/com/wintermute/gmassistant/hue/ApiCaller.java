package com.wintermute.gmassistant.hue;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wintermute.gmassistant.hue.model.CustomRequest;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiCaller
{
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

        CustomRequest req = new CustomRequest(method, url, reqBody, listener::onResponse,
            error -> listener.onError(String.valueOf(error)));
        requestQueue.add(req);
    }

    /**
     * Makes PUT call.
     *
     * @param ctx application context
     * @param url target
     * @param body content to send
     */
    public void putRequest(Context ctx, String url, String body)
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
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, url, reqBody, response ->
        {
        }, error -> System.out.println(error.toString()));
        requestQueue.add(req);
    }

    void customRequest(Context ctx, int method, String url, String body, CallbackListener listener)
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

        JsonObjectRequest req = new JsonObjectRequest(method, url, reqBody, listener::onResponse,
            error -> listener.onError(String.valueOf(error)));
        requestQueue.add(req);
    }
}
