package com.wintermute.soundboard.database.dto;

import lombok.Data;

/**
 * Represents a scene contained by playlist. A scene holds the track to play, light belonging to it and knows the next
 * track to play.
 *
 * @author wintermute
 */
@Data
public class Scene
{
    private String id;
    private String light;
    private String nextTrack;
}
