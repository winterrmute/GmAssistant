package com.wintermute.gmassistant.database.model;

import java.util.ArrayList;
import java.util.List;

public enum EffectDbModel
{
    TABLE_NAME("effects"),
    TRACK_ID("trackId"),
    GROUP_ID("groupId");

    private String column;

    EffectDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (EffectDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
