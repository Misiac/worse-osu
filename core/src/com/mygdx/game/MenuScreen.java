package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class MenuScreen implements Screen {

    Game game;
    Texture exitButton;
    Texture playButton;

    private static final int EXIT_BUTTON_WIDTH = 360;
    private static final int EXIT_BUTTON_HEIGHT = 340;
    private static final int PLAY_BUTTON_WIDTH = 360;
    private static final int PLAY_BUTTON_HEIGHT = 340;
    public static final int EXIT_BUTTON_Y = 100;
    public static final int PLAY_BUTTON_Y = 300;


    public MenuScreen(Game game) {
        this.game = game;
        playButton = new Texture("play.png");
        exitButton = new Texture("play.png"); // TODO: 07.08.2023
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(1, 0, 0, 1);

        int x = Game.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;

        game.batch.begin();
        game.batch.draw(playButton, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        game.batch.end();

        if (Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x && Game.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && Gdx.input.getY() > PLAY_BUTTON_Y) {
            if (Gdx.input.isTouched()){
                this.dispose();
                game.setScreen(new GameScreen(game));
            }
        }


    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

//Gdx.app.exit()
