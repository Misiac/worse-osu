package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class GameScreen implements Screen {

    Game game;
    Sound sound;
    private int circleX = 240;
    private int circleY = 240;


    public GameScreen(Game game) {
        this.game = game;
        sound = Gdx.audio.newSound(Gdx.files.internal("song.mp3"));
//        sound.play();
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

        Texture hitCircle = new Texture(Gdx.files.internal("hitcircle.png"));
        Texture hitCircleOverlay = new Texture(Gdx.files.internal("hitcircleoverlay.png"));
        Texture approachCircle = new Texture(Gdx.files.internal("approachcircle.png"));
        Texture circleNumber = new Texture(Gdx.files.internal("numbers/default-0.png"));

        game.batch.begin();
        game.batch.draw(hitCircle, circleX, circleY);
        game.batch.draw(hitCircleOverlay, circleX, circleY);
        game.batch.draw(approachCircle, circleX, circleY);
        game.batch.draw(circleNumber, circleX+39, circleY+39);   // 128:2 - 25/2
        game.batch.end();

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
