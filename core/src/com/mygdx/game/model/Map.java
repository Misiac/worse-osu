package com.mygdx.game.model;

import java.util.List;

public class Map {

    private final List<Mapset> mapsets;
    private final String bgPath;
    private final String audioPath;

    public List<Mapset> getMapsets() {
        return mapsets;
    }

    public Map(List<Mapset> mapsets, String audioPath, String bgPath) {
        this.mapsets = mapsets;
        this.audioPath = audioPath;
        this.bgPath = bgPath;
    }

    public String getAudioPath() {
        return audioPath;
    }

    public String getBgPath() {
        return bgPath;
    }

    public int lookupNumber(String mapsetVersion) {
        int i = 0;
        for (Mapset mapset : mapsets) {
            if (mapsetVersion.equals(mapset.getVersion())) return i;
            i++;
        }
        return -1;
    }
}
