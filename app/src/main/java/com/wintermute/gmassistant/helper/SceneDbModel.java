package com.wintermute.gmassistant.helper;

import java.util.ArrayList;
import java.util.List;

public enum SceneDbModel
{
    TABLE_NAME("scene"),
    ID("id"),
    EFFECT("effect"),
    MUSIC("music"),
    AMBIENCE("ambience"),
    NAME("name"),
    LIGHT("light");

    private String column;

    SceneDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (SceneDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
