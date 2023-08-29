package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.data.MapLoader;
import com.mygdx.game.screen.GameScreen;

import java.io.IOException;

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
    Texture background;
    BitmapFont bitmapFont;
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();


    public MenuScreen(Game game) {
        this.game = game;
        playButton = new Texture("play.png");
        exitButton = new Texture("play.png"); // TODO: 07.08.2023

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

        Gdx.gl.glClearColor(1, 0, 0, 1);


        int x = Game.WIDTH / 2 - PLAY_BUTTON_WIDTH / 2;

        game.batch.begin();

        game.batch.draw(background, 0, 0);
        bitmapFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bitmapFont.draw(game.batch, "Osu! Clone", x, (float) Gdx.graphics.getHeight() / 2 + 300);
        game.batch.draw(playButton, x, PLAY_BUTTON_Y, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
        game.batch.end();

        if (Gdx.input.getX() < x + PLAY_BUTTON_WIDTH && Gdx.input.getX() > x && Game.HEIGHT - Gdx.input.getY() < PLAY_BUTTON_Y + PLAY_BUTTON_HEIGHT && Gdx.input.getY() > PLAY_BUTTON_Y) {
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
