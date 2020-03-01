package com.wintermute.gmassistant.helper;

import java.util.ArrayList;
import java.util.List;

public enum TrackDbModel
{
    TABLE_NAME("track"),
    ID("id"),
    NAME("name"),
    PATH("path"),
    DURATION("duration"),
    ARTIST("artist");

    private String column;

    TrackDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (TrackDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
