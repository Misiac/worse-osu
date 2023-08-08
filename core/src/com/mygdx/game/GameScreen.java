package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.data.MapLoader;
import com.mygdx.game.model.CircleNumber;
import com.mygdx.game.model.HitObject;
import com.mygdx.game.model.Map;

public class GameScreen implements Screen {

    Game game;
    Sound sound;
    private int circleX = 240;
    private int circleY = 240;
    public static final long AR_OFFSET = 900;
    public int hitObjectScale;

    public static int resolutionMultiplier;
    long startTimeReference;
    long timeFromStart;
    Map map;


    public GameScreen(Game game) {
        this.game = game;
        sound = Gdx.audio.newSound(Gdx.files.internal("song.mp3"));
//        sound.play();
        resolutionMultiplier = Gdx.graphics.getWidth() / 640;
        this.map = MapLoader.Load();

        Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
        Cursor cursor = Gdx.graphics.newCursor(pm, 64, 64);
        pm.dispose();
        Gdx.graphics.setCursor(cursor);

        startTimeReference = System.currentTimeMillis();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeFromStart = System.currentTimeMillis() - startTimeReference;

        Texture hitCircle = new Texture(Gdx.files.internal("hitcircle.png"));
        Texture hitCircleOverlay = new Texture(Gdx.files.internal("hitcircleoverlay.png"));
        Texture approachCircle = new Texture(Gdx.files.internal("approachcircle.png"));


        game.batch.begin();

        int testCounter = 0; // TODO: 08.08.2023
        int approachCircleScale;
        for (HitObject hitObject : map.getHitObjects()) {

            if (timeFromStart > hitObject.getTime() - AR_OFFSET && timeFromStart < hitObject.getTime()) {
                Texture circleNumber = new Texture(Gdx.files.internal(
                        CircleNumber.valueOf("N" + testCounter).getPath()
                ));
                int calculatedX = hitObject.getOsuPixelX() * resolutionMultiplier;
                int calculatedY = hitObject.getOsuPixelY() * resolutionMultiplier;

                game.batch.draw(hitCircle, calculatedX, calculatedY);
                game.batch.draw(hitCircleOverlay, calculatedX, calculatedY);

                approachCircleScale = calculateApproachScale(timeFromStart, hitObject.getTime());
                hitObjectScale = 128 / 2 - approachCircleScale / 2;
                game.batch.draw(approachCircle, calculatedX + hitObjectScale, calculatedY + hitObjectScale, approachCircleScale, approachCircleScale);

                game.batch.draw(circleNumber, calculatedX + 39, calculatedY + 39);   // 128:2 - 25/2
            }
            testCounter++;
        }

        game.batch.end();


    }

    private int calculateApproachScale(long timeFromStart, long circleHitTime) {

        // MAX 256
        // MIN 128
        float input_range = 500;
        float output_range = 128;
        float scaling_factor = output_range / input_range;

        float scaledDifference = (circleHitTime - timeFromStart) * scaling_factor;
        float scaled_Value = scaledDifference + 128;

        System.out.println("Circle Time -> " + circleHitTime + " Start Time -> " + timeFromStart + " result -> " + scaled_Value);


        return (int) scaled_Value;


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
