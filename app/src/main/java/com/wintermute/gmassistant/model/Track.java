package com.wintermute.gmassistant.model;

import lombok.Data;

import java.io.Serializable;

/**
 * Represents audio file as an java object
 *
 * @author wintermute
 */
@Data
public class Track implements Serializable
{
    private Long id;
    private String name;
    private String path;
    private Long duration;
    private String artist;
    private String tag;
}
