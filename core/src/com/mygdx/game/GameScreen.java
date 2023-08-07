package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;

public class GameScreen implements Screen {

    Game game;
    Sound sound;


    public GameScreen(Game game) {
        this.game = game;
        sound = Gdx.audio.newSound(Gdx.files.internal("song.mp3"));
        sound.play();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
        Cursor cursor = Gdx.graphics.newCursor(pm, 64, 64);
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
