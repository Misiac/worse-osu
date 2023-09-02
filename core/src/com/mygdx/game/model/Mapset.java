package com.mygdx.game.model;

import java.util.List;

public class Mapset {

    private final List<HitObject> hitObjects;
    private final String version;
    private final String name;

    //future use
    private final double hpDrainRate;
    private final double circleSize;
    private final double approachRate;



    public Mapset(String name, List<HitObject> hitObjects, String version, double hpDrainRate,
                  double circleSize, double approachRate) {
        this.hitObjects = hitObjects;
        this.version = version;
        this.hpDrainRate = hpDrainRate;
        this.circleSize = circleSize;
        this.approachRate = approachRate;
        this.name = name;

    }

    public List<HitObject> getHitObjects() {
        return hitObjects;
    }

    public String getVersion() {
        return version;
    }

    public String getName() {
        return name;
    }
}
