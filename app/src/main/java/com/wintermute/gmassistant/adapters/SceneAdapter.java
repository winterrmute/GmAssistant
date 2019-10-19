package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.database.dao.LightDao;
import com.wintermute.gmassistant.database.dao.TrackDao;
import com.wintermute.gmassistant.database.dto.Scene;

import java.math.BigDecimal;
import java.util.List;

/**
 * Custom adapter for listing the scenes as ListView.
 */
public class SceneAdapter extends BaseAdapter
{
    Context ctx;
    private List<Scene> scenes;
    private LayoutInflater inflater;

    public SceneAdapter(Context ctx, List<Scene> scenes)
    {
        this.scenes = scenes;
        this.ctx = ctx;
        inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount()
    {
        return scenes.size();
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout result = (LinearLayout) inflater.inflate(R.layout.scene, parent, false);
        ImageView image = result.findViewById(R.id.color_image);
        TextView name = result.findViewById(R.id.scene_name);
        TextView startEffect = result.findViewById(R.id.effect);
        TextView music = result.findViewById(R.id.music);
        TextView ambience = result.findViewById(R.id.ambience);

        Scene target = scenes.get(position);
        String light = target.getLight();
        if (null != light && !"null".equals(light))
        {
            image.setImageBitmap(extractColor(light));
        }
        name.setText(target.getName());

        startEffect.setText(
            "Start: " + getTrackName(computeDisplayNameIfAbsent(target.getStartEffect()), target.getStartEffect()));
        music.setText("Next: " + getTrackName(computeDisplayNameIfAbsent(target.getStartEffect()), target.getBackgroundMusic()));
        ambience.setText("Ambience: " + getTrackName(computeDisplayNameIfAbsent(target.getStartEffect()), target.getBackgroundAmbience()));
        result.setTag(position);
        return result;
    }

    private String computeDisplayNameIfAbsent(String name){
        return name != null ? name : "not set";
    }

    /**
     * Get track name
     *
     * @param name of track.
     * @param trackId that should be added.
     * @return
     */
    private String getTrackName(String name, String trackId)
    {
        TrackDao dao = new TrackDao(ctx);
//        return computeDisplayNameIfAbsent(dao.computeTrackIfAbsent(trackId).getName());
        return name.equals("not set") ? "not set" : dao.computeTrackIfAbsent(trackId).getName();
    }

    /**
     * Extract bitmap from color of light.
     *
     * @param lightId to get its color
     * @return
     */
    private Bitmap extractColor(String lightId)
    {
        LightDao dao = new LightDao(ctx);
        Rect rect = new Rect(0, 0, 1, 1);
        Bitmap image = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        Paint paint = new Paint();
        paint.setColor(new BigDecimal(dao.getById(lightId).getColor()).intValue());
        canvas.drawRect(0, 0, 1, 1, paint);
        return image;
    }
}
