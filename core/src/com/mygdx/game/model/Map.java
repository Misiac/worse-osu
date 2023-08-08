package com.mygdx.game.model;

import java.util.ArrayList;
import java.util.List;

public class Map {

   private List<HitObject> hitObjects = new ArrayList<>();

    public Map(List<HitObject> hitObjects) {
        this.hitObjects = hitObjects;
    }

    public List<HitObject> getHitObjects() {
        return hitObjects;
    }

    public void setHitObjects(List<HitObject> hitObjects) {
        this.hitObjects = hitObjects;
    }
}
