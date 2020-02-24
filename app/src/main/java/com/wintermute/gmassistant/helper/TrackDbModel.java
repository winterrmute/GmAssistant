package com.wintermute.gmassistant.helper;

public enum TrackDbModel
{
    TABLE_NAME("track"),
    ID("id"),
    NAME("name"),
    PATH("path"),
    DURATION("duration"),
    ARTIST("name");

    private String column;

    TrackDbModel(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }
}
