package com.wintermute.gmassistant.helper;

public enum SceneDb
{
    TABLE_NAME("scene"),
    ID("id"),
    EFFECT("start_effect"),
    MUSIC("music"),
    AMBIENCE("ambience"),
    NAME("name"),
    LIGHT("light");

    private String column;

    SceneDb(String column)
    {
        this.column= column;
    }

    public String value(){
        return column;
    }
}
