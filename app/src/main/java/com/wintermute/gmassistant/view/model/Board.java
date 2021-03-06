package com.wintermute.gmassistant.view.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Board
{
    private Long id;
    private String name;
    private String type;
    private boolean parent;
    private boolean isRoot;
}
