package com.wintermute.soundboard.model;

import lombok.Data;

@Data
public class LightScene
{
    private long id;
    private String color;
    private int brightness;
    private int duration;
}
