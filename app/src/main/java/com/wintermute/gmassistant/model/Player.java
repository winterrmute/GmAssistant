package com.wintermute.gmassistant.model;

import android.media.MediaPlayer;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player
{
    private int id;
    private String tag;
    private Track track;
    private MediaPlayer mediaPlayer;
}
