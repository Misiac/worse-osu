package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screen.MenuScreen;

import java.util.List;

public class Game extends com.badlogic.gdx.Game {

    public static final int WIDTH = 1920;
    public static final int HEIGHT = 1080;
    public SpriteBatch batch;
    public List<String> files;

    @Override
    public void create() {
        batch = new SpriteBatch();
        this.setScreen(new MenuScreen(this));
        System.out.println("test");


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
