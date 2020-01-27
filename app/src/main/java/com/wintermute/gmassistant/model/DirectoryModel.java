package com.wintermute.gmassistant.model;

import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Map;

@Data
public class DirectoryModel
{
    private String name;
    private String path;
//    private Map<String, List<String>> subDirs;
    private Map<String, Object> subDirs;

    public DirectoryModel(String name, String path){
        this.name = name;
        this.path = path;
    }
}
