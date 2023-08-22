package com.mygdx.game.model;

import java.util.List;

public class Mapset {

    private List<HitObject> hitObjects;
    private String version;
    private double hpDrainRate;
    private double circleSize;
    private double approachRate;
    private String name;


    public Mapset(List<HitObject> testData) {
        this.hitObjects = testData; // testing

    }

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

    public void setHitObjects(List<HitObject> hitObjects) {
        this.hitObjects = hitObjects;
    }

    public String getVersion() {
        return version;
    }

    public double getHpDrainRate() {
        return hpDrainRate;
    }

    public double getCircleSize() {
        return circleSize;
    }

    public double getApproachRate() {
        return approachRate;
    }

    public String getName() {
        return name;
    }
}
