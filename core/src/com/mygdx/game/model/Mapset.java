package com.mygdx.game.model;

import java.util.ArrayList;
import java.util.List;

public class Mapset {

    private List<HitObject> hitObjects = new ArrayList<>();

    public Mapset(List<HitObject> testData) {
    }

    public List<HitObject> getHitObjects() {
        return hitObjects;
    }

    public void setHitObjects(List<HitObject> hitObjects) {
        this.hitObjects = hitObjects;
    }
}
