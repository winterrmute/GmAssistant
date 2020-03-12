package com.wintermute.gmassistant.hue.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents hue bulb
 *
 * @author wintermute
 */
@Data
@AllArgsConstructor
public class HueBulb
{
    private String name;
    private String type;
    private Long bridgeId;
    private boolean checked;
}
