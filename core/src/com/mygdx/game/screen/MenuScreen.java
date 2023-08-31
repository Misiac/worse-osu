package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.ButtonInfo;
import com.mygdx.game.Game;
import com.mygdx.game.data.MapLoader;
import com.mygdx.game.model.Map;

import java.io.IOException;
import java.util.LinkedHashMap;

public class MenuScreen implements Screen {

    Game game;
    Map draggedMap = null;
    Texture exitButton;
    Sprite exitButtonSprite;

    Texture playButton;
    Sprite playButtonSprite;

    Texture logo;
    Texture button;

    Texture info;
    Sprite infoSprite;

    private int x;
    java.util.Map<ButtonInfo, Sprite> buttonMap;

    private static final int BUTTON_WIDTH = 267;
    private static final int BUTTON_HEIGHT = 42;
    public static final int EXIT_BUTTON_Y = 100;
    public static final int PLAY_BUTTON_Y = 170;
    public static final float ACTIVE_BUTTON_SCALING_FACTOR = 1.1f;
    Texture background;

    BitmapFont bitmapFont;
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();


    public MenuScreen(Game game) {
        this.game = game;
        playButton = new Texture("menu/play.png");
        exitButton = new Texture("menu/exit.png"); // TODO: 07.08.2023
        logo = new Texture("menu/logo.png"); // TODO: 07.08.2023
        info = new Texture(Gdx.files.internal("menu/info.png"));
        button = new Texture(Gdx.files.internal("test.png"));
        playButtonSprite = new Sprite(playButton);
        exitButtonSprite = new Sprite(exitButton);
        infoSprite = new Sprite(info);
        buttonMap = new LinkedHashMap<>();

        x = Game.WIDTH / 8 - (playButton.getHeight() / 2);

        playButtonSprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        playButtonSprite.setColor(1, 1, 1, 0.7f);
        playButtonSprite.setCenter(x, PLAY_BUTTON_Y);

        exitButtonSprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButtonSprite.setColor(1, 1, 1, 0.7f);
        exitButtonSprite.setCenter(x, EXIT_BUTTON_Y);

        infoSprite.setColor(1, 1, 1, 0.7f);
        infoSprite.setCenter(Game.WIDTH * 0.8f, 300);

        background = new Texture(Gdx.files.internal("menubg.png"));

        parameter.size = 80;
        bitmapFont = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        game.batch.begin();

        game.batch.draw(background, 0, 0, Game.WIDTH, Game.HEIGHT);
        game.batch.draw(logo, x + 100, (float) Gdx.graphics.getHeight() / 2 + 300);
        infoSprite.draw(game.batch);

        if (checkIfObjectIsHoveredOver(x, PLAY_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT)) {

            playButtonSprite.setCenter(x + (BUTTON_WIDTH * ACTIVE_BUTTON_SCALING_FACTOR - BUTTON_WIDTH), PLAY_BUTTON_Y);
            playButtonSprite.setScale(ACTIVE_BUTTON_SCALING_FACTOR);
            playButtonSprite.draw(game.batch);

            if (isCorrectFileLoaded()) {

                if (Gdx.input.isTouched() && draggedMap != null) {
                    try {
                        game.setScreen(new GameScreen(game, draggedMap, 0));
                        this.dispose();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

        } else {
            playButtonSprite.setScale(1);
            playButtonSprite.setCenter(x, PLAY_BUTTON_Y);
            playButtonSprite.draw(game.batch);
        }
        if (checkIfObjectIsHoveredOver(x, EXIT_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT)) {

            exitButtonSprite.setCenter(x + (BUTTON_WIDTH * ACTIVE_BUTTON_SCALING_FACTOR - BUTTON_WIDTH), EXIT_BUTTON_Y);
            exitButtonSprite.setScale(ACTIVE_BUTTON_SCALING_FACTOR);
            exitButtonSprite.draw(game.batch);

            if (Gdx.input.isTouched()) {
                Gdx.app.exit();
            }
        } else {
            exitButtonSprite.setScale(1);
            exitButtonSprite.setCenter(x, EXIT_BUTTON_Y);
            exitButtonSprite.draw(game.batch);
        }

        for (ButtonInfo buttonInfo : buttonMap.keySet()) {
            Sprite thisSprite = buttonMap.get(buttonInfo);
            thisSprite.draw(game.batch);
        }
        game.batch.end();

        if (isCorrectFileLoaded()) { //
            try {
                if (draggedMap == null) {
                    draggedMap = MapLoader.Load(game.files.get(0));

                    draggedMap.getMapsets().forEach(mapset -> {
                        ButtonInfo buttonInfo = new ButtonInfo(mapset.getVersion());
                        Sprite sprite = new Sprite(button);
                        sprite.setCenter(buttonInfo.getXCoordinate(), buttonInfo.getYCoordinate());
                        sprite.setColor(1, 1, 1, 0.7f);
                        buttonMap.put(buttonInfo, sprite);
                    });
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isCorrectFileLoaded() {
        if (!game.files.isEmpty()) {
            if (MapLoader.isProperOszFile(game.files.get(0))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfObjectIsHoveredOver(int x, int y, int objectWidth, int objectHeight) {

        if (Gdx.input.getX() > x - objectWidth / 2 &&
                Gdx.input.getX() < x + objectWidth / 2 &&
                Gdx.input.getY() > Game.HEIGHT - (y + objectHeight / 2) &&
                Gdx.input.getY() < Game.HEIGHT - (y - objectHeight / 2)) {
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void show() {

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