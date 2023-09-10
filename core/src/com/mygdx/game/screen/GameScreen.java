package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.Game;
import com.mygdx.game.ScrollProcessor;
import com.mygdx.game.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class GameScreen implements Screen {

    Game game;
    Map map;

    // CONSTANTS
    private static final long AR_OFFSET = 900;
    private static final int BLUE_HIT_MULTIPLIER = 50;
    private static final int GREEN_HIT_MULTIPLIER = 100;
    private static final int PERFECT_HIT_MULTIPLIER = 300;
    private static final int OBJECT_SIDE_LENGTH = 128;
    private static final int NUMBER_OBJECT_LENGTH = 50;
    private static final int CIRCLE_CURSOR_OFFSET = OBJECT_SIDE_LENGTH / 2 - NUMBER_OBJECT_LENGTH / 2;

    private static final int KEYBOARD_KEY_ONE = Input.Keys.Z;
    private static final int KEYBOARD_KEY_TWO = Input.Keys.X;

    private static final int MOUSE_BUTTON_ONE = Input.Buttons.LEFT;
    private static final int MOUSE_BUTTON_TWO = Input.Buttons.RIGHT;

    // COLLECTIONS
    private final List<HitObject> currentHitObjects = new LinkedList<>();
    private final List<HitObject> pastHitObjects = new LinkedList<>();
    private final List<HitObject> futureHitObjects = new LinkedList<>();
    private final List<VisualEffect> visualEffects = new LinkedList<>();

    // GAMEPLAY FIELDS
    private int count0;
    private int count50;
    private int count100;
    private int count300;
    private final int mapsetNumber;
    private int combo;
    private int maxCombo;
    private long score;
    private final long musicId;
    private int health; // between 0 - 700
    private String accuracy;
    private long lastHitObjectTime;

    // SCREEN FIELDS
    private static int xOffset;
    private static int yOffset;
    private final int newHeight;
    private static double resolutionMultiplierY;
    private final long startTimeReference;
    private long timeFromStart;

    // FONTS
    private BitmapFont bitmapComboFont;
    private BitmapFont bitmapScoreFont;

    // TEXTURES
    private Texture hitCircle;
    private Texture hitCircleOverlay;
    private Texture approachCircle;
    private Texture miss;
    private Texture hit50;
    private Texture hit100;
    private Texture healthBarBg;
    private Sprite backgroundSprite;
    private TextureRegion healthBarRegion;

    // SOUND
    private Sound music;
    private Sound hitsound;
    private Sound comboBreak;

    // INPUT
    private final ScrollProcessor scrollProcessor = new ScrollProcessor();
    private final InputMultiplexer inputMultiplexer = new InputMultiplexer();


    public GameScreen(Game game, Map map, int mapsetNumber) {
        this.game = game;

        int newWidth = (int) (640 * (Gdx.graphics.getHeight() / 480.0));
        newHeight = (int) (480 * (Gdx.graphics.getHeight() / 480.0));

        xOffset = ((Gdx.graphics.getWidth() - newWidth) / 2) + 64; // maximum object x property is 512
        yOffset = 48; // maximum object y property is 384
        resolutionMultiplierY = (Gdx.graphics.getHeight() / 480.0);

        this.map = map;
        this.mapsetNumber = mapsetNumber;

        prepareObjects();
        musicId = music.play(scrollProcessor.getMusicVolume());
        startTimeReference = System.currentTimeMillis();

    }

    private void prepareObjects() {

        hitCircle = new Texture(Gdx.files.internal("game/hitcircle.png"));
        hitCircleOverlay = new Texture(Gdx.files.internal("game/hitcircleoverlay.png"));
        approachCircle = new Texture(Gdx.files.internal("game/approachcircle.png"));
        miss = new Texture(Gdx.files.internal("game/hit0.png"));
        hit50 = new Texture(Gdx.files.internal("game/hit50.png"));
        hit100 = new Texture(Gdx.files.internal("game/hit100.png"));
        healthBarBg = new Texture(Gdx.files.internal("game/scorebar-bg.png"));
        Texture healthBarColor = new Texture(Gdx.files.internal("game/scorebar-colour.png"));
        Texture background = new Texture(Gdx.files.internal(map.getBgPath()));
        healthBarRegion = new TextureRegion(healthBarColor, 0, 0, 700, healthBarColor.getHeight());
        backgroundSprite = new Sprite(background);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        music = Gdx.audio.newSound(Gdx.files.internal(map.getAudioPath()));
        hitsound = Gdx.audio.newSound(Gdx.files.internal("game/hitsound.ogg"));
        comboBreak = Gdx.audio.newSound(Gdx.files.internal("game/combobreak.wav"));

        combo = 0;
        maxCombo = 0;
        score = 0;
        count0 = 0;
        count50 = 0;
        count100 = 0;
        count300 = 0;
        health = 700;
        inputMultiplexer.addProcessor(scrollProcessor);
        Gdx.input.setInputProcessor(inputMultiplexer);

        backgroundSprite.setColor(1, 1, 1, 0.1f);
        futureHitObjects.addAll(map.getMapsets().get(mapsetNumber).getHitObjects()); // add all objects at start from source
        backgroundSprite.setSize(Game.WIDTH, Game.HEIGHT);

        lastHitObjectTime = futureHitObjects.get(futureHitObjects.size() - 1).getTime() + 500;

        parameter.size = 80;
        bitmapComboFont = generator.generateFont(parameter);
        parameter.size = 45;
        bitmapScoreFont = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        timeFromStart = System.currentTimeMillis() - startTimeReference;
        music.setVolume(musicId, scrollProcessor.getMusicVolume());
        filterHitObjects();
        healthBarRegion.setRegionWidth(calculateHealth());

        accuracy = calculateAccuracy();

        game.batch.begin();

        backgroundSprite.draw(game.batch);
        game.batch.draw(healthBarBg, 0, Game.HEIGHT - healthBarBg.getHeight());
        game.batch.draw(healthBarRegion, 3, Game.HEIGHT - (float) healthBarBg.getHeight() / 2 - 3);

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
        bitmapScoreFont.draw(game.batch, accuracy,
                (float) Game.WIDTH - ((float) Game.WIDTH / 24),
                (float) Game.HEIGHT - 10 - 40, 70.0F,
                0,
                false
        );

        int approachCircleScale;

        for (HitObject hitObject : currentHitObjects) {

            Texture circleNumber = CircleNumber.valueOf("N" + hitObject.getNumber()).getTexture();
            int calculatedX = calculateObjectXPosition(hitObject.getOsuPixelX());
            int calculatedY = calculateObjectYPosition(hitObject.getOsuPixelY());

            game.batch.draw(hitCircle, calculatedX, calculatedY);
            game.batch.draw(hitCircleOverlay, calculatedX, calculatedY);

            approachCircleScale = calculateApproachScale(timeFromStart, hitObject.getTime());
            int hitObjectScale = OBJECT_SIDE_LENGTH / 2 - approachCircleScale / 2;
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
                iterator.remove();
            }
        }
        game.batch.end();

        if (checkIfUserHasClicked()) {  // if user clicked anywhere
            for (HitObject hitObject : currentHitObjects) {
                boolean wasHit = checkIfObjectsWasPressed(calculateObjectXPosition(hitObject.getOsuPixelX()),
                        calculateObjectYPosition(hitObject.getOsuPixelY()));
                if (wasHit) {
                    handleHitObjectHit(hitObject);
                    break;
                }
            }
        }
        checkAndHandleGameEnd();
    }

    private void checkAndHandleGameEnd() {

        if (health <= 0 || timeFromStart > (lastHitObjectTime + 1000)) { // user has lost or completed the map
            boolean loseFlag = health <= 0;
            music.stop();
            game.setScreen(new ScoreScreen(game, new Score(
                    map.getMapsets().get(mapsetNumber).getName(),
                    LocalDate.now().toString(),
                    LocalTime.now().toString().substring(0, 8),
                    count300,
                    count100,
                    count50,
                    count0,
                    maxCombo,
                    score,
                    accuracy,
                    loseFlag
            )));
        }
    }

    private int calculateHealth() {
        health -= 1;
        return health;
    }

    private void handleHitObjectHit(HitObject hitObject) { // circle was properly hit
        pastHitObjects.add(hitObject);
        currentHitObjects.remove(hitObject);
        hitsound.play(scrollProcessor.getEffectVolume());
        combo++;
        if (combo > maxCombo) maxCombo = combo;

        long difference = hitObject.getTime() - timeFromStart;
        if (difference > 120) {
            if (difference > 180) {  // blue hit (50)
                visualEffects.add(new VisualEffect(hit50,
                        calculateObjectXPosition(hitObject.getOsuPixelX()),
                        calculateObjectYPosition(hitObject.getOsuPixelY())));
                addHealth(10);
                count50++;
                score += (long) BLUE_HIT_MULTIPLIER * (1 + (combo)) / 25;
            } else { // green hit (100)
                visualEffects.add(new VisualEffect(hit100,
                        calculateObjectXPosition(hitObject.getOsuPixelX()),
                        calculateObjectYPosition(hitObject.getOsuPixelY())));
                count100++;
                addHealth(30);
                score += (long) GREEN_HIT_MULTIPLIER * (1 + (combo)) / 25;
            }
        } else { // perfect hit
            count300++;
            score += (long) PERFECT_HIT_MULTIPLIER * (1 + (combo)) / 25;
            addHealth(50);
        }
    }

    private void addHealth(int amount) {
        if (health + amount > 700) {
            health = 700;
        } else health += amount;
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

    private void filterHitObjects() { //splits hit objects to three lists based on time and user play
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
                comboBreak.play(scrollProcessor.getEffectVolume());
                combo = 0; // reset combo
                count0++;
                VisualEffect visualEffect = new VisualEffect(miss, calculateObjectXPosition(hitObject.getOsuPixelX()),
                        calculateObjectYPosition(hitObject.getOsuPixelY()));
                visualEffects.add(visualEffect);
                addHealth(-50);
            }
        }
    }

    private boolean checkIfObjectsWasPressed(int x, int y) {

        int inputX = Gdx.input.getX();
        int inputY = Math.abs(Gdx.input.getY() - newHeight); // Y 0 for cursor is top screen but for textures is down so there is a need for conversion

        double distance = Math.sqrt(
                Math.pow(x - (inputX - 64), 2) + Math.pow(y - (inputY - 64), 2));
        return distance < 64;
    }

    private int calculateApproachScale(long timeFromStart, long circleHitTime) {

        float input_range = 1000 - (float) AR_OFFSET / 2;
        float output_range = 128;
        float scaling_factor = output_range / input_range;

        float scaledDifference = (circleHitTime - timeFromStart) * scaling_factor;
        float scaled_Value = scaledDifference + 128;

        return (int) scaled_Value;
    }

    private String calculateAccuracy() {
        double accuracy;
        String accuracyString;

        double total = 300 * count300 + 100 * count100 + 50 * count50;
        double countTotal = 300 * (count300 + count100 + count50 + count0);

        accuracy = total / countTotal;
        if (Double.isNaN(accuracy)) {
            accuracy = 100;
        } else accuracy = accuracy * 100;

        if (accuracy == 100) {
            accuracyString = "100.0%";

        } else {
            try {
                accuracyString = String.valueOf(accuracy).substring(0, 4) + "%";
            } catch (StringIndexOutOfBoundsException e) {
                accuracyString = String.valueOf(accuracy).substring(0, 3) + "0" + "%";
            }
        }
        return accuracyString;
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
