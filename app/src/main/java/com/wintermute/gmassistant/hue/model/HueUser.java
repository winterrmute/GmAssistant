package com.wintermute.gmassistant.hue.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HueUser
{
    private String ip;
    private String username;
}
