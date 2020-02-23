package com.wintermute.gmassistant.helper;

public enum Categories
{
    MUSIC("music"),
    AMBIENCE("ambience"),
    EFFECT("effect");

    private String category;

    Categories(String category)
    {
        this.category = category;
    }

    public String value(){
        return category;
    }
}
