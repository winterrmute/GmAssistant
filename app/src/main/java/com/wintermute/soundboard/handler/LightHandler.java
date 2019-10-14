package com.wintermute.soundboard.handler;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.wintermute.soundboard.database.dto.Light;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class LightHandler
{

    private static final String HUE_USERNAME = "BwQwczBxyonBkXGuXAm93QMN7JJfX2HOUwyvmnps";
    private static final String HUE_URL = "http://192.168.40.4";
    private static final String HUE_LIGHTS_CONTROL = HUE_URL + "/api/" + HUE_USERNAME + "/lights";
    private static final String light1 = "4";
    private static final String light2 = "5";
    private static final String light3 = "12";
    private Context ctx;
    private Light light;

    public LightHandler(Context ctx, Light light)
    {
        this.ctx = ctx;
        this.light = light;
    }

    private JSONObject buildHueReq()
    {
        double[] xy = getRGBtoXY(Color.valueOf(new BigDecimal(light.getColor()).intValue()));
        try
        {
            return new JSONObject(
                "{ \"on\":true, \"bri\": 10, \"xy\": [ " + xy[0] + ", " + xy[1] + " ], \"bri\": "+ light.getBrightness() + ", \"transitiontime\": 1, \"hue\": "
                    + "46920 }");
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public void manageLight() {
        String[] lights = new String[] {"4", "5", "12"};
        for (int i = 0; i < lights.length; i++)
        {
            req(lights[i]);
        }
    }

    public void req(String id)
    {
        JsonObjectRequest jsonObjectRequest =
            new JsonObjectRequest(Request.Method.PUT, HUE_LIGHTS_CONTROL + "/"+ id + "/state", buildHueReq(),
                response -> Log.e("Response: ", response.toString()), error ->
            {
                // TODO: Handle error
                Log.e("MESSAGE:", error.getMessage());
            });

        // Access the RequestQueue through your singleton class.
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
