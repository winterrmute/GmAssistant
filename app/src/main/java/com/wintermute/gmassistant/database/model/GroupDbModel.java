package com.wintermute.gmassistant.database.model;

import java.util.ArrayList;
import java.util.List;

public enum GroupDbModel
{
    TABLE_NAME("groups"),
    ID("id"),
    NAME("name");

    private String column;

    GroupDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (GroupDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
