package com.wintermute.gmassistant.database.model;

import java.util.ArrayList;
import java.util.List;

public enum LightConfigDbModel
{
    TABLE_NAME("light_config"),
    ID("id"),
    IP_ADDRESS("ip_address"),
    USERNAME("username");

    private String column;

    LightConfigDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (LightConfigDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
