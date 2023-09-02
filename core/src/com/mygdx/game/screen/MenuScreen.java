package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.ButtonInfo;
import com.mygdx.game.Game;
import com.mygdx.game.data.MapLoader;

import java.io.IOException;
import java.util.LinkedHashMap;

public class MenuScreen implements Screen {


    Game game;

    Texture exitButton;
    Sprite exitButtonSprite;

    Texture playButton;
    Sprite playButtonSprite;

    Texture logo;
    Texture button;

    Texture info;
    Sprite infoSprite;
    Sprite mapsetsInfo;

    int choosenMapsetNumber;
    private final int x;
    java.util.Map<ButtonInfo, Sprite> buttonMap;

    private static final int BUTTON_WIDTH = 267;
    private static final int BUTTON_HEIGHT = 42;
    public static final int EXIT_BUTTON_Y = 100;
    public static final int PLAY_BUTTON_Y = 170;
    public static final float ACTIVE_BUTTON_SCALING_FACTOR = 1.1f;
    private static final float MAPSET_BUTTON_WIDTH = 485;
    private static final float MAPSET_BUTTON_HEIGHT = 40;
    Texture background;

    BitmapFont bitmapFont;
    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/MonospaceTypewriter.ttf"));
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
        mapsetsInfo = new Sprite(new Texture(Gdx.files.internal("menu/mapsets.png")));
        infoSprite = new Sprite(info);
        buttonMap = new LinkedHashMap<>();
        ButtonInfo.resetY();
        x = Game.WIDTH / 8 - (playButton.getHeight() / 2);

        playButtonSprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        playButtonSprite.setColor(1, 1, 1, 0.7f);
        playButtonSprite.setCenter(x, PLAY_BUTTON_Y);

        exitButtonSprite.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButtonSprite.setColor(1, 1, 1, 0.7f);
        exitButtonSprite.setCenter(x, EXIT_BUTTON_Y);

        infoSprite.setColor(1, 1, 1, 0.7f);
        infoSprite.setCenter(Game.WIDTH * 0.8f, 300);

        mapsetsInfo.setCenter(ButtonInfo.getXCoordinate(), Game.HEIGHT - 100);
        mapsetsInfo.setColor(1, 1, 1, 0.9f);

        background = new Texture(Gdx.files.internal("menubg.png"));

        parameter.size = 24;
        bitmapFont = generator.generateFont(parameter);
        generator.dispose();

        Pixmap pm = new Pixmap(Gdx.files.internal("game/cursor.png"));
        Cursor cursor = Gdx.graphics.newCursor(pm, 64, 64);
        pm.dispose();
        Gdx.graphics.setCursor(cursor);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        game.batch.begin();

        game.batch.draw(background, 0, 0, Game.WIDTH, Game.HEIGHT);
        game.batch.draw(logo, x + 100, (float) Gdx.graphics.getHeight() / 2 + 300);
        infoSprite.draw(game.batch);
        mapsetsInfo.draw(game.batch);

        if (checkIfObjectIsHoveredOver(x, PLAY_BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT)) {

            playButtonSprite.setCenter(x + (BUTTON_WIDTH * ACTIVE_BUTTON_SCALING_FACTOR - BUTTON_WIDTH), PLAY_BUTTON_Y);
            playButtonSprite.setScale(ACTIVE_BUTTON_SCALING_FACTOR);
            playButtonSprite.draw(game.batch);

            if (isCorrectFileLoaded()) {

                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && Game.draggedMap != null) {
                    game.setScreen(new GameScreen(game, Game.draggedMap, choosenMapsetNumber));
                    this.dispose();
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

            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                Gdx.app.exit();
            }
        } else {
            exitButtonSprite.setScale(1);
            exitButtonSprite.setCenter(x, EXIT_BUTTON_Y);
            exitButtonSprite.draw(game.batch);
        }

        for (ButtonInfo buttonInfo : buttonMap.keySet()) { // render buttons
            Sprite thisSprite = buttonMap.get(buttonInfo);

            if (checkIfObjectIsHoveredOver(ButtonInfo.getXCoordinate(), buttonInfo.getYCoordinate(), (int) MAPSET_BUTTON_WIDTH, (int) MAPSET_BUTTON_HEIGHT)) {
                thisSprite.setCenter(ButtonInfo.getXCoordinate() - 24, buttonInfo.getYCoordinate());
                thisSprite.setScale(ACTIVE_BUTTON_SCALING_FACTOR);
                thisSprite.draw(game.batch);
                bitmapFont.getData().setScale(1.12f);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    choosenMapsetNumber = Game.draggedMap.lookupNumber(buttonInfo.getMapsetVersion());
                }


            } else {
                thisSprite.setScale(1);
                thisSprite.setCenter(ButtonInfo.getXCoordinate(), buttonInfo.getYCoordinate());
                thisSprite.draw(game.batch);
                bitmapFont.getData().setScale(1);
            }
            bitmapFont.draw(game.batch, buttonInfo.getMapsetVersion(), ButtonInfo.getXCoordinate() - MAPSET_BUTTON_WIDTH / 2 + 10, buttonInfo.getYCoordinate() + MAPSET_BUTTON_HEIGHT / 4);
        }

        game.batch.end();

        if (game.wasMapChanged()) { // resets buttons if different map was dragged
            buttonMap.clear();
            ButtonInfo.resetY();
        }

        if (isCorrectFileLoaded()) {
            try {
                if (buttonMap.isEmpty()) {
                    Game.draggedMap = MapLoader.Load(game.files.get(0));

                    Game.draggedMap.getMapsets().forEach(mapset -> {
                        ButtonInfo buttonInfo = new ButtonInfo(mapset.getVersion());
                        Sprite sprite = new Sprite(button);
                        sprite.setSize(MAPSET_BUTTON_WIDTH, MAPSET_BUTTON_HEIGHT);
                        sprite.setCenter(ButtonInfo.getXCoordinate(), buttonInfo.getYCoordinate());
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
            return MapLoader.isProperOszFile(game.files.get(0));
        } else return Game.draggedMap != null;
    }

    private boolean checkIfObjectIsHoveredOver(int x, int y, int objectWidth, int objectHeight) {

        return Gdx.input.getX() > x - objectWidth / 2 &&
                Gdx.input.getX() < x + objectWidth / 2 &&
                Gdx.input.getY() > Game.HEIGHT - (y + objectHeight / 2) &&
                Gdx.input.getY() < Game.HEIGHT - (y - objectHeight / 2);
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