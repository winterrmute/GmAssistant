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
    private String effectPath;
    private String musicPath;
    private String ambiencePath;

    public void setEffectPath(String effectPath)
    {
        this.effectPath = computeNameIfAbsent(effectPath);
    }

    public void setMusicPath(String musicPath)
    {
        this.musicPath = computeNameIfAbsent(musicPath);
    }

    public void setAmbiencePath(String ambiencePath)
    {
        this.ambiencePath = computeNameIfAbsent(ambiencePath);
    }

    private String computeNameIfAbsent(String path){
        return null == path ? "" : path;
    }
}
