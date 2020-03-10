package com.wintermute.gmassistant.database.model;

import java.util.ArrayList;
import java.util.List;

public enum HueBridgeDbModel
{
    TABLE_NAME("hue_bridges"),
    ID("id"),
    NAME("name"),
    IP_ADDRESS("ip"),
    USERNAME("username"),
    ACTIVE("active");

    private String column;

    HueBridgeDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (HueBridgeDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
