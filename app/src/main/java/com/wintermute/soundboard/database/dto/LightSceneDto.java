package com.wintermute.soundboard.database.dto;

import lombok.Data;

@Data
public class LightSceneDto
{
    private long id;
    private String color;
    private int brightness;
    private int duration;
}
