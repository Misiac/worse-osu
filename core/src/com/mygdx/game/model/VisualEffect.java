package com.mygdx.game.model;

import com.badlogic.gdx.graphics.Texture;

public class VisualEffect {
    private int timer;
    private Texture texture;
    private int xCoordinate;
    private int yCoordinate;

    public VisualEffect(Texture texture, int xCoordinate, int yCoordinate) {
        this.timer = 30;
        this.texture = texture;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public float getTimer() {
        return timer;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void decrementTimer() {
        this.timer--;
    }
}
