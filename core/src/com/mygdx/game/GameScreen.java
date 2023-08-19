package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.data.MapLoader;
import com.mygdx.game.model.CircleNumber;
import com.mygdx.game.model.HitObject;
import com.mygdx.game.model.Map;
import com.mygdx.game.model.VisualEffect;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class GameScreen implements Screen {

    Game game;
    Sound music;
    Sound hitsound;
    Sound comboBreak;

    public static final long AR_OFFSET = 900;
    public static final int BLUE_HIT_MULTIPLIER = 50;
    public static final int GREEN_HIT_MULTIPLIER = 100;
    public static final int PERFECT_HIT_MULTIPLIER = 300;
    public static final int OBJECT_SIDE_LENGTH = 128;
    public static final int NUMBER_OBJECT_LENGTH = 50;
    private static final int CIRCLE_CURSOR_OFFSET = OBJECT_SIDE_LENGTH / 2 - NUMBER_OBJECT_LENGTH / 2;

    private static final int KEYBOARD_KEY_ONE = Input.Keys.Z;
    private static final int KEYBOARD_KEY_TWO = Input.Keys.X;

    public static final int MOUSE_BUTTON_ONE = Input.Buttons.LEFT;
    public static final int MOUSE_BUTTON_TWO = Input.Buttons.RIGHT;
    private static final float EFFECT_VOLUME = 0.5f;
    private static final float MUSIC_VOLUME = 0.5f;

    public int hitObjectScale;

    public List<HitObject> currentHitObjects = new LinkedList<>();
    public List<HitObject> pastHitObjects = new LinkedList<>();
    public List<HitObject> futureHitObjects = new LinkedList<>();
    public List<VisualEffect> visualEffects = new LinkedList<>();
    public int count0;
    public int count50;
    public int count100;
    public int count300;


    public static double resolutionMultiplierY;

    public static int xOffset;
    public static int yOffset;
    int newHeight;
    int newWidth;
    long startTimeReference;
    long timeFromStart;
    Map map;
    int combo;
    int objectsUntilNow;
    long score;

    BitmapFont bitmapComboFont;
    BitmapFont bitmapScoreFont;
    FreeTypeFontGenerator generator;
    FreeTypeFontGenerator.FreeTypeFontParameter parameter;

    Texture hitCircle;
    Texture hitCircleOverlay;
    Texture approachCircle;
    Texture miss;
    Texture hit50;
    Texture hit100;

    ScrollProcessor scrollProcessor = new ScrollProcessor();
    InputMultiplexer inputMultiplexer = new InputMultiplexer();
    long musicId;
    Texture background;
    Sprite backgroundSprite;


    public GameScreen(Game game) throws IOException {  // TODO: 09.08.2023 throws
        this.game = game;

        newWidth = (int) (640 * (Gdx.graphics.getHeight() / 480.0)); // works and is 1440
        newHeight = (int) (480 * (Gdx.graphics.getHeight() / 480.0)); // works and is 1080

        xOffset = ((Gdx.graphics.getWidth() - newWidth) / 2) + 64; // maximum object x property is 512
        yOffset = 48; // maximum object y property is 384

        resolutionMultiplierY = (Gdx.graphics.getHeight() / 480.0); // same but for Y
        this.map = MapLoader.Load(game.files.get(0));

        prepareObjects();

        Pixmap pm = new Pixmap(Gdx.files.internal("cursor.png"));
        Cursor cursor = Gdx.graphics.newCursor(pm, 64, 64);
        pm.dispose();
        Gdx.graphics.setCursor(cursor);

        startTimeReference = System.currentTimeMillis();
        parameter.size = 80;
        bitmapComboFont = generator.generateFont(parameter);
        parameter.size = 45;
        bitmapScoreFont = generator.generateFont(parameter);
        generator.dispose();

        musicId = music.play(scrollProcessor.getMusicVolume());

    }

    private void prepareObjects() {

        hitCircle = new Texture(Gdx.files.internal("hitcircle.png"));
        hitCircleOverlay = new Texture(Gdx.files.internal("hitcircleoverlay.png"));
        approachCircle = new Texture(Gdx.files.internal("approachcircle.png"));
        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        music = Gdx.audio.newSound(Gdx.files.internal(map.getAudioPath()));
        hitsound = Gdx.audio.newSound(Gdx.files.internal("hitsound.ogg"));
        comboBreak = Gdx.audio.newSound(Gdx.files.internal("combobreak.wav"));
        miss = new Texture(Gdx.files.internal("hit0.png"));
        hit50 = new Texture(Gdx.files.internal("hit50.png"));
        hit100 = new Texture(Gdx.files.internal("hit100.png"));
        combo = 0;
        score = 0;
        count0 = 0;
        count50 = 0;
        count100 = 0;
        count300 = 0;
        objectsUntilNow = 0;
        inputMultiplexer.addProcessor(scrollProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);
        background = new Texture(Gdx.files.internal(map.getBgPath()));
        backgroundSprite = new Sprite(background);
        backgroundSprite.setColor(1, 1, 1, 0.1f);
        futureHitObjects.addAll(map.getMapsets().get(0).getHitObjects()); // add all objects at start from source
        backgroundSprite.setSize(Game.WIDTH,Game.HEIGHT);
    }
    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeFromStart = System.currentTimeMillis() - startTimeReference;

        double accuracy = calculateAccuracy();
        String accuracyString;

        if (Double.isInfinite(accuracy)) {
            accuracyString = "100.00";
        } else {
            try {
                accuracyString = String.valueOf(accuracy).substring(0, 4);
            } catch (StringIndexOutOfBoundsException e) {
                accuracyString = String.valueOf(accuracy).substring(0, 3) + "0";
            }
        }


        filterHitObjects();

        game.batch.begin();
        backgroundSprite.draw(game.batch);

        bitmapComboFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        bitmapScoreFont.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        bitmapComboFont.draw(game.batch, combo + "x", 5, 70); // combo drawer
        bitmapScoreFont.draw(game.batch,
                score + "",
                (float) Game.WIDTH - ((float) Game.WIDTH / 9),
                (float) Game.HEIGHT - 10,
                200.0F,
                0,
                false);
        bitmapScoreFont.draw(game.batch, accuracyString,
                (float) Game.WIDTH - ((float) Game.WIDTH / 24),
                (float) Game.HEIGHT - 10 - 40, 70.0F,
                0,
                false
        );


        int approachCircleScale;

        for (
                HitObject hitObject : currentHitObjects) {

            Texture circleNumber = new Texture(Gdx.files.internal( // maybe preload all numbers from 0-9?
                    CircleNumber.valueOf("N" + hitObject.getNumber()).getPath()
            ));
            int calculatedX = calculateObjectXPosition(hitObject.getOsuPixelX());
            int calculatedY = calculateObjectYPosition(hitObject.getOsuPixelY());

            game.batch.draw(hitCircle, calculatedX, calculatedY);
            game.batch.draw(hitCircleOverlay, calculatedX, calculatedY);

            approachCircleScale = calculateApproachScale(timeFromStart, hitObject.getTime());
            hitObjectScale = OBJECT_SIDE_LENGTH / 2 - approachCircleScale / 2;
            game.batch.draw(approachCircle, calculatedX + hitObjectScale, calculatedY + hitObjectScale, approachCircleScale, approachCircleScale);

            game.batch.draw(circleNumber, calculatedX + CIRCLE_CURSOR_OFFSET, calculatedY + CIRCLE_CURSOR_OFFSET);   // 128:2 - 25/2

        }

        ListIterator<VisualEffect> iterator = visualEffects.listIterator(); // visual effect drawer
        while (iterator.hasNext()) {
            VisualEffect visualEffect = iterator.next();
            if (visualEffect.getTimer() != 0) {
                game.batch.draw(visualEffect.getTexture(),
                        visualEffect.getXCoordinate(),
                        visualEffect.getYCoordinate());
                visualEffect.decrementTimer();
            } else {
                visualEffect = null;
                iterator.remove();
            }
        }
        game.batch.end();

        if (

                checkIfUserHasClicked()) {

            for (HitObject hitObject : currentHitObjects) {
                boolean wasHit = checkIfObjectsWasPressed(calculateObjectXPosition(hitObject.getOsuPixelX()),
                        calculateObjectYPosition(hitObject.getOsuPixelY()));
                if (wasHit) {
                    handleHitObjectHit(hitObject);
                    break;
                }
            }
        }
        music.setVolume(musicId, scrollProcessor.getMusicVolume());
    }

    private void handleHitObjectHit(HitObject hitObject) { // circle was properly hit
        pastHitObjects.add(hitObject);
        currentHitObjects.remove(hitObject);
        hitsound.play(scrollProcessor.getEffectVolume());
        combo++;

        long difference = hitObject.getTime() - timeFromStart;
        if (difference > 120) {
            if (difference > 180) {  // blue hit (50)
                visualEffects.add(new VisualEffect(hit50,
                        calculateObjectXPosition(hitObject.getOsuPixelX()),
                        calculateObjectYPosition(hitObject.getOsuPixelY())));
                count50++;
                score += (long) BLUE_HIT_MULTIPLIER * (1 + (combo)) / 25;
            } else { // green hit (100)
                visualEffects.add(new VisualEffect(hit100,
                        calculateObjectXPosition(hitObject.getOsuPixelX()),
                        calculateObjectYPosition(hitObject.getOsuPixelY())));
                count100++;
                score += (long) GREEN_HIT_MULTIPLIER * (1 + (combo)) / 25;
            }
        } else { // perfect hit
            count300++;
            score += (long) PERFECT_HIT_MULTIPLIER * (1 + (combo)) / 25;
        }

        objectsUntilNow++;
    }

    private int calculateObjectXPosition(int osuPixelX) { // calculates x property from osu pixel format to current user resolution
        return (int) ((osuPixelX * resolutionMultiplierY) + xOffset);

    }

    private int calculateObjectYPosition(int osuPixelY) { // calculates y property from osu pixel format to current user resolution
        return (int) Math.abs((osuPixelY * resolutionMultiplierY + yOffset) - Gdx.graphics.getHeight()) - yOffset * 3;
    }

    private boolean checkIfUserHasClicked() {
        return Gdx.input.isKeyJustPressed(KEYBOARD_KEY_ONE) || Gdx.input.isKeyJustPressed(KEYBOARD_KEY_TWO)
                || Gdx.input.isButtonJustPressed(MOUSE_BUTTON_ONE) || Gdx.input.isButtonJustPressed(MOUSE_BUTTON_TWO);
    }

    private void filterHitObjects() {
        ListIterator<HitObject> listIterator = futureHitObjects.listIterator();

        while (listIterator.hasNext()) { // filters future hit objects into currently rendered
            HitObject hitObject = listIterator.next();

            if (!pastHitObjects.contains(hitObject) && timeFromStart > hitObject.getTime() - AR_OFFSET && timeFromStart < hitObject.getTime()) {//timeframe of being rendered
                if (!currentHitObjects.contains(hitObject)) {
                    currentHitObjects.add(hitObject);
                    listIterator.remove();
                    continue;
                }
            }
            if (hitObject.getTime() > timeFromStart + AR_OFFSET) { // after timeframe of being rendered so breaks useless looping
                break;
            }
        }
        listIterator = currentHitObjects.listIterator();
        while (listIterator.hasNext()) { // filters currently rendered objects into past ones if they are after their time
            HitObject hitObject = listIterator.next();
            if (timeFromStart >= hitObject.getTime()) { // if is after being rendered (MISS)
                listIterator.remove();
                pastHitObjects.add(hitObject);
                comboBreak.play(EFFECT_VOLUME);
                combo = 0; // reset combo
                count0++;
                VisualEffect visualEffect = new VisualEffect(miss, calculateObjectXPosition(hitObject.getOsuPixelX()),
                        calculateObjectYPosition(hitObject.getOsuPixelY()));
                visualEffects.add(visualEffect);
//                System.out.println("miss");
            }
        }
    }

    private boolean checkIfObjectsWasPressed(int x, int y) {

        int inputX = Gdx.input.getX();
        int inputY = Math.abs(Gdx.input.getY() - newHeight); // Y 0 for cursor is top screen but for textures is down so there is a need for conversion

        double distance = Math.sqrt(
                Math.pow(x - (inputX - 64), 2) + Math.pow(y - (inputY - 64), 2)
        );
//        System.out.println("distance -> " + distance + " gdxX -> " + inputX + " gdxY -> " + inputY + " circleX -> " + x + " circleY -> " + y); // TODO: 10.08.2023
        if (distance < 64) {
//            System.out.println("REGISTERED CLICK");
//            System.out.println(distance);
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

        return (int) scaled_Value;

    }

    private double calculateAccuracy() {
        double total = 300 * count300 + 100 * count100 + 50 + count50;
        double countTotal = 300 * (count300 + count100 + count50 + count0);
        double accuracy;
            accuracy = total / countTotal;
        return accuracy * 100;
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
