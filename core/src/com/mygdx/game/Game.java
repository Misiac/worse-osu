package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.model.Map;
import com.mygdx.game.screen.MenuScreen;

import java.util.List;

public class Game extends com.badlogic.gdx.Game {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public static Map draggedMap = null;
    public SpriteBatch batch;
    public List<String> files;
    public String mapFilePath;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MenuScreen(this));
        mapFilePath = "";

    }

    public boolean wasMapChanged() {
        try {
            if (files == null) return false;
            else {
                if (mapFilePath.equals(files.get(0))) {
                    return false;
                } else {
                    mapFilePath = files.get(0);
                    return true;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    public Game(List<String> files) {
        this.files = files;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {

    }
}
