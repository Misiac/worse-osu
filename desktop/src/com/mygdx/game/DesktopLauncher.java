package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3WindowAdapter;

import java.util.LinkedList;
import java.util.List;


public class DesktopLauncher {
    public static List<String> files = new LinkedList<>();

    public void addFile(String file) {
        files.add(file);
    }

    public static void main(String[] arg) {
        final DesktopLauncher test = new DesktopLauncher();
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setResizable(false);
        config.setWindowedMode(Game.WIDTH, Game.HEIGHT);
        config.setTitle("Osu! Clone");


        config.setWindowListener(new Lwjgl3WindowAdapter() {
            @Override
            public void filesDropped(String[] files) {
                for (String file : files) {
                    test.addFile(file);
                }
            }
        });
        new Lwjgl3Application(new Game(files), config);
    }
}

