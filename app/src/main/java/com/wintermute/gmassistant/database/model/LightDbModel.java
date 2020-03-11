package com.wintermute.gmassistant.database.model;

import java.util.ArrayList;
import java.util.List;

public enum LightDbModel
{
    TABLE_NAME("lights"),
    ID("id"),
    COLOR("color"),
    BRIGHTNESS("brightness");

    private String column;

    LightDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (LightDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
