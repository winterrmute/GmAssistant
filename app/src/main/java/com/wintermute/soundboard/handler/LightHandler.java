package com.wintermute.soundboard.handler;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class LightHandler
{

    private static final String HUE_USERNAME = "BwQwczBxyonBkXGuXAm93QMN7JJfX2HOUwyvmnps";
    private static final String HUE_URL = "http://192.168.40.4";
    private static final String HUE_LIGHTS_CONTROL = HUE_URL + "/api/" + HUE_USERNAME + "/lights";
    private Context ctx;
    private Color color;
    private int brightness;

    public LightHandler(Context ctx, Color color, int brightness)
    {
        this.ctx = ctx;
        this.color = color;
        this.brightness = brightness;
    }

    private JSONObject changeColorAndBrightness()
    {
        double[] xy = getRGBtoXY(color);
        try
        {
            return new JSONObject(
                "{ \"on\":true, \"bri\": 10, \"xy\": [ " + xy[0] + ", " + xy[1] + " ], \"bri\": " + brightness
                    + ", \"transitiontime\": 1, \"hue\": " + "46920 }");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject resetToDefault()
    {
        try
        {
            return new JSONObject(
                "{ \"on\":true, \"bri\": 10, \"xy\": [ 0.31822298615917416, 0.32930599409450195 ], \"bri\": 255, "
                    + "\"transitiontime\": 1, \"hue\": " + "46920 }");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void setLight(boolean reset)
    {
        String[] lights = new String[] {"4", "5", "12"};
        for (int i = 0; i < lights.length; i++)
        {
            req(lights[i], reset);
        }
    }

    public void req(String id, boolean reset)
    {
        JSONObject requestBody;
        if (reset)
        {
            requestBody = resetToDefault();
        } else
        {
            requestBody = changeColorAndBrightness();
        }

        JsonObjectRequest jsonObjectRequest = null;
        try
        {
            jsonObjectRequest =
                new JsonObjectRequest(Request.Method.PUT, HUE_LIGHTS_CONTROL + "/" + id + "/state", requestBody,
                    response -> Log.e("Response: ", response.toString()), error ->
                {
                    //nothing to do
                });
        } catch (NullPointerException e)
        {
            //nothing to do
        }
        RequestQueue que = Volley.newRequestQueue(ctx);
        que.add(jsonObjectRequest);
    }

    /**
     * Extract RGB and convert it to xy coordinates.
     *
     * @param pickedColor
     * @return coordinates
     */
    private double[] getRGBtoXY(Color pickedColor)
    {
        float[] colors = {pickedColor.red(), pickedColor.green(), pickedColor.blue()};

        for (int i = 0; i < colors.length; i++)
        {
            colors[i] = normalizeColor(colors[i]);
        }

        double xRaw = (float) (colors[0] * 0.649926 + colors[1] * 0.103455 + colors[2] * 0.197109);
        double yRaw = (float) (colors[0] * 0.234327 + colors[1] * 0.743075 + colors[2] * 0.022598);
        double zRaw = (float) (colors[0] * 0.0000000 + colors[1] * 0.053077 + colors[2] * 1.035763);

        return new double[] {(xRaw / (xRaw + yRaw + zRaw)), (yRaw / (xRaw + yRaw + zRaw))};
    }

    /**
     * @param color picked.
     * @return normalized color.
     */
    private float normalizeColor(float color)
    {
        float normalizedToOne = color / 255;
        if ((color / 255) > 0.04045)
        {
            return (float) Math.pow((normalizedToOne + 0.055) / (1.0 + 0.055), 2.4);
        } else
        {
            return (float) (normalizedToOne / 12.92);
        }
    }
}
