package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

import java.util.LinkedList;
import java.util.List;

public class GameScreen implements Screen {


    Game game;
    Sound sound;
    public static final long AR_OFFSET = 900;
    public int hitObjectScale;
    public List<HitObject> currentHitObjects = new LinkedList<>();

    public static int resolutionMultiplierX;
    public static int resolutionMultiplierY;
    long startTimeReference;
    long timeFromStart;
    Map map;
    public static final int OBJECT_SIDE_LENGTH = 128;
    public static final int NUMBER_OBJECT_LENGTH = 50;
    private static final int CIRCLE_CURSOR_OFFSET = OBJECT_SIDE_LENGTH / 2 - NUMBER_OBJECT_LENGTH / 2;


    public GameScreen(Game game) {
        this.game = game;
        sound = Gdx.audio.newSound(Gdx.files.internal("song.mp3"));
//        sound.play();
        resolutionMultiplierX = Gdx.graphics.getWidth() / 640;  // 640 is default for osuPixel X in osu file format
        resolutionMultiplierY = Gdx.graphics.getHeight() / 480; // same but for Y
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

                if (!currentHitObjects.contains(hitObject)) currentHitObjects.add(hitObject);

                Texture circleNumber = new Texture(Gdx.files.internal(
                        CircleNumber.valueOf("N" + testCounter).getPath()
                ));
                int calculatedX = hitObject.getOsuPixelX() * resolutionMultiplierX;
                int calculatedY = hitObject.getOsuPixelY() * resolutionMultiplierY;

                game.batch.draw(hitCircle, calculatedX, calculatedY);
                game.batch.draw(hitCircleOverlay, calculatedX, calculatedY);

                approachCircleScale = calculateApproachScale(timeFromStart, hitObject.getTime());
                hitObjectScale = OBJECT_SIDE_LENGTH / 2 - approachCircleScale / 2;
                game.batch.draw(approachCircle, calculatedX + hitObjectScale, calculatedY + hitObjectScale, approachCircleScale, approachCircleScale);

                game.batch.draw(circleNumber, calculatedX + CIRCLE_CURSOR_OFFSET, calculatedY + CIRCLE_CURSOR_OFFSET);   // 128:2 - 25/2
            }
            testCounter++;
        }
//        System.out.println("------------");


        game.batch.end();


        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {

            for (HitObject hitObject : currentHitObjects) {
                boolean wasPressed = checkIfObjectsWasPressed(hitObject.getOsuPixelX() * resolutionMultiplierX,
                        hitObject.getOsuPixelY() * resolutionMultiplierY);

            }
        }

    }

    private boolean checkIfObjectsWasPressed(int x, int y) {

        int inputX = Gdx.input.getX();
        int inputY = Math.abs(Gdx.input.getY() - Gdx.graphics.getHeight()); // Y 0 for cursor is top screen but for textures is down so there is a need for conversion

        double distance = Math.sqrt(
                Math.pow(x - (inputX - 64), 2) + Math.pow(y - (inputY - 64), 2)
        );
        System.out.println("distance -> " + distance + " gdxX -> " + inputX + " gdxY -> " + inputY + " circleX -> " + x + " circleY -> " + y);
        if (distance < 64) {
            System.out.println("REGISTERED CLICK");
            System.out.println(distance);
            return true;

        }
        return false;
    }

    private int calculateApproachScale(long timeFromStart, long circleHitTime) {

        float input_range = 1000 - (float) AR_OFFSET / 2;
        float output_range = 128;
        float scaling_factor = output_range / input_range;

        float scaledDifference = (circleHitTime - timeFromStart) * scaling_factor;
        float scaled_Value = scaledDifference + 128;

//        System.out.println("Circle Time -> " + circleHitTime + " Start Time -> " + timeFromStart + " result -> " + scaled_Value); // TODO: 08.08.2023


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
