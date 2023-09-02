package com.mygdx.game.model;

public abstract class HitObject {
    int osuPixelX;
    int osuPixelY;
    long time;
    int type;
    int number;

    public HitObject(int osuPixelX, int osuPixelY, long time, int type, int number) {
        this.osuPixelX = osuPixelX;
        this.osuPixelY = osuPixelY;
        this.time = time;
        this.type = type;
        this.number = number;
    }

    public int getOsuPixelX() {
        return osuPixelX;
    }

    public int getOsuPixelY() {
        return osuPixelY;
    }

    public long getTime() {
        return time;
    }

    public int getNumber() {
        return number;
    }
}

