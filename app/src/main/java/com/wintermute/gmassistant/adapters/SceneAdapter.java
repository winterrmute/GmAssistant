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
import com.wintermute.gmassistant.model.Scene;
import com.wintermute.gmassistant.operations.SceneOperations;

import java.math.BigDecimal;
import java.util.List;

/**
 * Custom adapter for listing the scenes as ListView.
 */
public class SceneAdapter extends BaseAdapter
{
    private Context ctx;
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

        SceneOperations operations = new SceneOperations(ctx);
        Scene target = operations.getScene(scenes.get(position));

        Long light = target.getLight() != null ? target.getLight().getId() : 0L;
        if (light != 0L)
        {
            image.setImageBitmap(extractColor(light));
        }
        name.setText(target.getName());

        String trackName = target.getEffect() != null ? target.getEffect().getName() : "";
        startEffect.setText("Effect: " + trackName);

        trackName = target.getMusic() != null ? target.getMusic().getName() : "";
        music.setText("Music: " + trackName);

        trackName = target.getAmbience() != null ? target.getAmbience().getName() : "";
        ambience.setText("Ambience: " + trackName);
        result.setTag(position);
        return result;
    }

    /**
     * Extract bitmap from color of light.
     *
     * @param lightId to get its color
     * @return
     */
    private Bitmap extractColor(Long lightId)
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
