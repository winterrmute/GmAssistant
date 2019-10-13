package com.wintermute.soundboard.database.dto;

import lombok.Data;

/**
 * Represents audio file as an java object
 *
 * @author wintermute
 */
@Data
public class TrackDto
{
    private String id;
    private String name;
    private String artist;
    private String tag;
    private String path;
    private String sceneId;
}
