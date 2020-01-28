package com.wintermute.gmassistant.database.dto;

import lombok.Data;

/**
 * Represents directory model
 *
 * @author wintermute
 */
@Data
public class Directory
{
    Long id;
    String path;
    String tag;
    Boolean recursive;
}
