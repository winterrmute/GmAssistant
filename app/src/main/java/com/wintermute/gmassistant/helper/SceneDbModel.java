package com.wintermute.gmassistant.helper;

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
}
