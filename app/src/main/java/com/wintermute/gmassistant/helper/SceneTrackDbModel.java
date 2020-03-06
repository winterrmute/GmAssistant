package com.wintermute.gmassistant.helper;

import java.util.ArrayList;
import java.util.List;

public enum SceneTrackDbModel
{
    TABLE_NAME("scene_track_config"),
    ID("id"),
    TRACK_ID("trackId"),
    VOLUME("volume"),
    DELAY("delay");

    private String column;

    SceneTrackDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (SceneTrackDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
