package com.wintermute.soundboard.database.dao;

import lombok.Data;

/**
 * Represents Light Scene data access object.
 *
 * @author wintermute
 */
@Data
public class LightDao
{
    private String id;
    private String color;
    private String brightness;
}
