package com.wintermute.gmassistant.database.model;

import java.util.ArrayList;
import java.util.List;

public enum HueBulbDbModel
{
    TABLE_NAME("hue_bulbs"),
    ID("id"),
    IP_ADDRESS("name"),
    USERNAME("type");

    private String column;

    HueBulbDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (HueBulbDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
