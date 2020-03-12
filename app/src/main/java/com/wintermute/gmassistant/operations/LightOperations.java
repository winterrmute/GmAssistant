package com.wintermute.gmassistant.operations;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.wintermute.gmassistant.database.dao.LightDao;
import com.wintermute.gmassistant.hue.ApiCaller;
import com.wintermute.gmassistant.view.model.Light;

import java.math.BigDecimal;
import java.util.Map;

/**
 * This class is responsible for informing views that data has changed, doing basic operations on models and receiving
 * events from views.
 *
 * @author wintermute
 */
public class LightOperations
{
    private Context ctx;
    private LightDao dao;

    /**
     * Creates an instance
     *
     * @param ctx application context
     */
    public LightOperations(Context ctx)
    {
        this.ctx = ctx;
        dao = new LightDao(ctx);
    }

    /**
     * Extract bitmap from color of light.
     *
     * @param lightId to get its color
     * @return
     */
    private Bitmap extractColor(Long lightId)
    {
        LightOperations operations = new LightOperations(ctx);
        Light light = operations.getLight(lightId);

        LightDao dao = new LightDao(ctx);
        Rect rect = new Rect(0, 0, 1, 1);
        Bitmap image = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setColor(new BigDecimal(light.getColor()).intValue());
        canvas.drawRect(0, 0, 1, 1, paint);
        return image;
    }

    private Light getLight(Long lightId)
    {
        Light result = new Light();
        Map<String, Object> lightData = dao.get(lightId);
        result.setId((Long) lightData.get("id"));
        result.setColor((String) lightData.get("color"));
        result.setBrightness((Long) lightData.get("brightness"));
        return result;
    }

    /**
     * Creates new scene
     *
     * @param content all scene information
     */
    public void createLight(Map<String, Object> content)
    {
        Light light = new Light();
        storeLight(content);
    }

    private void storeLight(Map<String, Object> content)
    {
        ContentValues values = new ContentValues();
        values.put("color", (String) content.get("brightness"));
        values.put("brightness", (Long) content.get("brightness"));
        dao.insert(values);
    }

    /**
     * Extract RGB and convert it to xy coordinates.
     *
     * @param pickedColor
     * @return coordinates
     */
    public double[] getRGBtoXY(Color pickedColor)
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

    public void changeLight(double[] color, long brightness)
    {

    }
}
