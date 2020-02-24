package com.wintermute.gmassistant.model;

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
    private Long id;
    private String name;
    private Light light;
    private Track effect;
    private Track music;
    private Track ambience;
}
