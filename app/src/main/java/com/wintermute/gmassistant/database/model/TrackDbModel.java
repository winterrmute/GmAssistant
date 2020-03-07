package com.wintermute.gmassistant.database.model;

import java.util.ArrayList;
import java.util.List;

public enum TrackDbModel
{
    TABLE_NAME("tracks"),
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
