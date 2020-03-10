package com.wintermute.gmassistant.hue.model;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Custom JsonRequest. Takes JSONObject as request and responds with JSONArray.
 *
 * @author wintermute
 */
import java.io.UnsupportedEncodingException;

public class CustomRequest extends JsonRequest<JSONArray>
{
    public CustomRequest(int method, String url, JSONObject req, Response.Listener<JSONArray> listener,
                         Response.ErrorListener errorListener)
    {
        super(method, url, (req == null) ? null : req.toString(), listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response)
    {
        try
        {
            String jsonString =
                new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            String jsonArray;
            if (jsonString.startsWith("{")){
                jsonArray = new StringBuilder("[").append(jsonString).append("]").toString();
                return Response.success(new JSONArray(jsonArray), HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JSONException e)
        {
            return Response.error(new ParseError(e));
        }
    }
}
