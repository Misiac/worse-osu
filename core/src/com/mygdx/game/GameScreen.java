package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

public class GameScreen implements Screen {

    Game game;

    public GameScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
        Cursor cursor = Gdx.graphics.newCursor(pm, 0, 0);
        pm.dispose();
        Gdx.graphics.setCursor(cursor);
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
