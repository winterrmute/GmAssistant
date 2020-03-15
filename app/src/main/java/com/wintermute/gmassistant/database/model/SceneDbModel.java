package com.wintermute.gmassistant.database.model;

import java.util.ArrayList;
import java.util.List;

public enum SceneDbModel
{
    TABLE_NAME("scenes"),
    ID("id"),
    EFFECT("effect"),
    MUSIC("music"),
    AMBIENCE("ambience"),
    NAME("name"),
    LIGHT("light"),
    BOARD_ID("boardId");

    private String attr;

    SceneDbModel(String column)
    {
        this.attr = column;
    }

    public String value(){
        return attr;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (SceneDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
