package com.mygdx.game;

public class ButtonInfo {
    private String mapsetVersion;
    private static float xCoordinate;
    private float yCoordinate;
    private static float previousY = 500;

    public ButtonInfo(String mapsetVersion) {
        this.mapsetVersion = mapsetVersion;
        yCoordinate = previousY + 40f;
        previousY = yCoordinate;
        xCoordinate = 500;
    }

    public String getMapsetVersion() {
        return mapsetVersion;
    }

    public float getXCoordinate() {
        return xCoordinate;
    }

    public float getYCoordinate() {
        return yCoordinate;
    }


}
