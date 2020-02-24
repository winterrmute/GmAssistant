package com.wintermute.gmassistant.helper;

public enum Tags
{
    MUSIC("music"),
    AMBIENCE("ambience"),
    EFFECT("effect");

    private String category;

    Tags(String category)
    {
        this.category = category;
    }

    public String value(){
        return category;
    }
}
