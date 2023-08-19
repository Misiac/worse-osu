package com.mygdx.game.model;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<Mapset> mapsets = new ArrayList<>();
    private String bgPath;
    private String audioPath;

    public List<Mapset> getMapsets() {
        return mapsets;
    }

    public void setMapsets(List<Mapset> mapsets) {
        this.mapsets = mapsets;
    }

    public Map(List<Mapset> mapsets, String audioPath, String bgPath) {
        this.mapsets = mapsets;
        this.audioPath = audioPath;
        this.bgPath = bgPath;
    }

    public Map(Mapset mapset) {
        this.mapsets.add(mapset);
    }

    public String getAudioPath() {
        return audioPath;
    }

    public String getBgPath() {
        return bgPath;
    }
}
