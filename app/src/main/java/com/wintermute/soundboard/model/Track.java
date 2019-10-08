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
    private String id;
    private String name;
    private String artist;
    private String path;
    private String scene_id;
}
