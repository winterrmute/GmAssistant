package com.wintermute.gmassistant.view.model;

import androidx.lifecycle.ViewModel;
import com.wintermute.gmassistant.model.LibraryFile;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
public class AudioLibrary extends ViewModel
{
    private Map<Integer, List<LibraryFile>> audioLibrary;

    public Map<Integer, List<LibraryFile>> getAudioLibrary() {
        if (audioLibrary == null) {
            audioLibrary = new HashMap<>();
        }
        return audioLibrary;
    }
}
