package com.mygdx.game.model;

public abstract class HitObject {
    int osuPixelX;
    int osuPixelY;
    int time;
    int type;

    public HitObject(int osuPixelX, int osuPixelY, int time, int type) {
        this.osuPixelX = osuPixelX;
        this.osuPixelY = osuPixelY;
        this.time = time;
        this.type = type;
    }

    public int getOsuPixelX() {
        return osuPixelX;
    }

    public int getOsuPixelY() {
        return osuPixelY;
    }

    public int getTime() {
        return time;
    }

    public int getType() {
        return type;
    }

}

