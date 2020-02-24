package com.wintermute.gmassistant.model;

import lombok.Data;

/**
 * Represents audio file as an java object
 *
 * @author wintermute
 */
@Data
public class Track
{
    private Long id;
    private String name;
    private String path;
    private Long duration;
    private String artist;
    private String tag;
}
