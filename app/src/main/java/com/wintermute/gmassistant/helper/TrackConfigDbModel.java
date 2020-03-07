package com.wintermute.gmassistant.helper;

import java.util.ArrayList;
import java.util.List;

public enum TrackConfigDbModel
{
    TABLE_NAME("scene_track_config"),
    SCENE_ID("sceneId"),
    TRACK_ID("trackId"),
    VOLUME("volume"),
    DELAY("delay");

    private String column;

    TrackConfigDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (TrackConfigDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
