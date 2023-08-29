package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.Game;
import com.mygdx.game.data.MapLoader;

import java.io.IOException;

public class MenuScreen implements Screen {

    Game game;
    Texture exitButton;
    Sprite exitButtonSprite;
    Texture playButton;
    Sprite playButtonSprite;
    Texture logo;
    private int x;

    private static final int BUTTON_WIDTH = 267;
    private static final int BUTTON_HEIGHT = 42;
    public static final int EXIT_BUTTON_Y = 100;
    public static final int PLAY_BUTTON_Y = 170;
    Texture background;

    BitmapFont bitmapFont;
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();


    public MenuScreen(Game game) {
        this.game = game;
        playButton = new Texture("play.png");
        exitButton = new Texture("exit.png"); // TODO: 07.08.2023
        logo = new Texture("logo.png"); // TODO: 07.08.2023
        playButtonSprite = new Sprite(playButton);
        exitButtonSprite = new Sprite(exitButton);
        x = Game.WIDTH / 8 - (playButton.getHeight() / 2);

        playButtonSprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        playButtonSprite.setColor(1, 1, 1, 0.7f);
        playButtonSprite.setCenter(x, PLAY_BUTTON_Y);

        exitButtonSprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButtonSprite.setColor(1, 1, 1, 0.7f);
        exitButtonSprite.setCenter(x, EXIT_BUTTON_Y);

        background = new Texture(Gdx.files.internal("menubg.png"));

        parameter.size = 80;
        bitmapFont = generator.generateFont(parameter);
        generator.dispose();


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        game.batch.begin();

        game.batch.draw(background, 0, 0, Game.WIDTH, Game.HEIGHT);
        game.batch.draw(logo, x + 100, (float) Gdx.graphics.getHeight() / 2 + 300);
        playButtonSprite.draw(game.batch);
        exitButtonSprite.draw(game.batch);
        game.batch.end();

        if (Gdx.input.getX() > x - BUTTON_WIDTH / 2 && // if play button is hovered over
                Gdx.input.getX() < x + BUTTON_WIDTH / 2 &&
                Gdx.input.getY() > Game.HEIGHT - (PLAY_BUTTON_Y + BUTTON_HEIGHT / 2) &&
                Gdx.input.getY() < Game.HEIGHT - (PLAY_BUTTON_Y - BUTTON_HEIGHT / 2)) {

            if (Gdx.input.isTouched()) {
                if (!game.files.isEmpty()) { // file drop listener
                    this.dispose();
                    if (isProperOszFile(game.files.get(game.files.size() - 1))) {
                        // load map here
                        try {
                            game.setScreen(new GameScreen(game));
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }

        }
        if (Gdx.input.getX() > x - BUTTON_WIDTH / 2 && // if exit button is hovered over
                Gdx.input.getX() < x + BUTTON_WIDTH / 2 &&
                Gdx.input.getY() > Game.HEIGHT - (EXIT_BUTTON_Y + BUTTON_HEIGHT / 2) &&
                Gdx.input.getY() < Game.HEIGHT - (EXIT_BUTTON_Y - BUTTON_HEIGHT / 2)) {

            if (Gdx.input.isTouched()) {
                Gdx.app.exit();
            }

        }

    }

    private boolean isProperOszFile(String path) {

        String extension = MapLoader.getExtension(path);
        if (extension.equals("osz")) {
            return true;
        }
        return false;
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
