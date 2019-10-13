package com.wintermute.soundboard.database.dao;

import lombok.Data;

/**
 * Represents Scene data access object.
 *
 * @author wintermute
 */
@Data
public class SceneDao
{
    String id;
    String trackId;
    String nextTrack;
    String lightId;
}
