package com.wintermute.soundboard.model;

import lombok.Data;

/**
 * Represents audio file as an java object
 *
 * @author wintermute
 */
@Data
public class Track
{
    private long id;
    private String name;
    private String artist;
    private String path;
    private long scene_id;
}
