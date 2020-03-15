package com.wintermute.gmassistant.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.wintermute.gmassistant.R;
import com.wintermute.gmassistant.operations.LightOperations;
import com.wintermute.gmassistant.view.model.Light;
import com.wintermute.gmassistant.view.model.Scene;
import com.wintermute.gmassistant.view.model.Track;

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
        Scene target = scenes.get(position);

        Light light = target.getLight();
        if (light != null)
        {

            LightOperations operations = new LightOperations(ctx);
            image.setImageBitmap(operations.extractColor(light));
        }
        name.setText(target.getName());

        startEffect.setText("Effect: " + computeName(target.getEffect()));
        music.setText("Music: " + computeName(target.getMusic()));
        ambience.setText("Ambience: " + computeName(target.getAmbience()));
        result.setTag(position);
        return result;
    }

    private String computeName(Track track)
    {
        String result = track != null ? track.getName() : "";
        return result.length() > 30 ? result.substring(0, 27) + "..." : result;
    }

    public void updateDisplayedElements(List<Scene> scenes)
    {
        this.scenes = scenes;
        notifyDataSetChanged();
    }
}
