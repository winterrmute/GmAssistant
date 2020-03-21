package com.wintermute.gmassistant.database.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum BoardDbModel
{
    TABLE_NAME("boards"),
    ID("id"),
    NAME("name"),
    TYPE("type"),
    IS_PARENT("isParent"),
    IS_ROOT("isRoot"),
    NESTED_BOARDS_TABLE_NAME("nested_boards"),
    NESTED_BOARD_ID("boardId"),
    NESTED_BOARD_PARENT_ID("parentId");

    private String column;

    BoardDbModel(String column)
    {
        this.column = column;
    }

    public String value()
    {
        return column;
    }

    public static List<String> getValues()
    {
        List<String> result = new ArrayList<>();
        for (BoardDbModel value : values())
        {
            result.add(value.value());
        }
        return result;
    }

    public static List<String> getAttrs()
    {
        return Arrays.asList(ID.value(), NAME.value(), TYPE.value(), IS_PARENT.value());
    }
}
