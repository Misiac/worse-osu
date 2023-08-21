package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class Game extends com.badlogic.gdx.Game {

    public static final int WIDTH = 1600;
    public static final int HEIGHT = 900;
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
