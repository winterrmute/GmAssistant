package com.wintermute.gmassistant.database.model;

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

    public String value()
    {
        return category;
    }

    public static String getByOrdinalWithDefault(int ordinal)
    {
        if (ordinal == 0)
        {
            return MUSIC.value();
        } else if (ordinal == 1)
        {
            return AMBIENCE.value();
        } else if (ordinal == 2)
        {
            return EFFECT.value();
        } else
        {
            return MUSIC.value();
        }
    }
}
