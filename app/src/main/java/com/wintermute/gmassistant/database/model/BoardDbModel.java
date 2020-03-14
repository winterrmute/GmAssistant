package com.wintermute.gmassistant.database.model;

import java.util.ArrayList;
import java.util.List;

public enum BoardDbModel
{
    TABLE_NAME("boards"),
    ID("id"),
    NAME("name"),
    TYPE("type");

    private String column;

    BoardDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }

    public static List<String> getValues(){
        List<String> result = new ArrayList<>();
        for (BoardDbModel value : values()) {
            result.add(value.value());
        }
        return result;
    }
}
