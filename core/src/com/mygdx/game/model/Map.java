package com.mygdx.game.model;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<Mapset> mapsets = new ArrayList<>();

    public List<Mapset> getMapsets() {
        return mapsets;
    }

    public void setMapsets(List<Mapset> mapsets) {
        this.mapsets = mapsets;
    }

    public Map(List<Mapset> mapsets) {
        this.mapsets = mapsets;
    }

    public Map(Mapset mapset) {
        this.mapsets.add(mapset);
    }


}
