package com.mygdx.game;

public class ButtonInfo {
    private final String mapsetVersion;
    private static final int xCoordinate;
    private final int yCoordinate;
    private static int PREVIOUS_Y = Game.HEIGHT - 100 - 50;

    static {
        xCoordinate = (int) (Game.WIDTH * 0.8f);
    }

    public ButtonInfo(String mapsetVersion) {
        this.mapsetVersion = mapsetVersion;
        yCoordinate = PREVIOUS_Y - 50;
        PREVIOUS_Y = yCoordinate;

    }

    public String getMapsetVersion() {
        return mapsetVersion;
    }

    public static int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public static void resetY() {
        PREVIOUS_Y = Game.HEIGHT - 100 - 50;
    }

}
