package com.wintermute.gmassistant.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents file collected by {@link com.wintermute.gmassistant.services.FileBrowserService}
 */
@Data
@AllArgsConstructor
public class FileElement
{
    private String name;
    private String path;
    private boolean isRoot;
}
