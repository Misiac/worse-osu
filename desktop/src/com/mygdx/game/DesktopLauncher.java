package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;


public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setResizable(false);
        config.setWindowedMode(Game.WIDTH, Game.HEIGHT);
        config.setTitle("Osu Clone");
        new Lwjgl3Application(new Game(), config);
    }
}

