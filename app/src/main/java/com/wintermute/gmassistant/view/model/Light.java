package com.wintermute.gmassistant.view.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Light implements Serializable
{
    private Long id;
    private String color;
    private String brightness;
}
