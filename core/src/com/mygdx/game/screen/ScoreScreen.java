package com.mygdx.game.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.Game;
import com.mygdx.game.model.Score;

public class ScoreScreen implements Screen {
    private final Game game;
    private final Score score;

    Texture hit300;
    Texture hit100;
    Texture hit50;
    Texture hit0;
    Texture combo;
    Texture acc;
    Texture header;
    Texture scoreSign;
    Texture grade;
    Texture backButton;
    Sprite headerSprite;
    BitmapFont headerFont;
    BitmapFont scoreFont;
    Sound uiClick;

    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/roboto.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    Texture background = new Texture(Gdx.files.internal("menubg.png"));
    public static final int OFFSET = 5;
    public static final int HEADER_FONT_SIZE = 35;
    public static final int SCORE_FONT_SIZE = 70;
    int valueOffset;
    int textureHeightOffset;

    // TABLE CONSTANTS
    public static final int TABLE_ROW_1_X = (int) (Game.WIDTH * 0.07);
    public static final int TABLE_ROW_2_X = (int) (Game.WIDTH * 0.3);

    public static final int TABLE_ROW_1_Y = (int) (Game.HEIGHT * 0.7);

    public static final int TABLE_ROW_2_Y = (int) (Game.HEIGHT * 0.5);

    public static final int TABLE_ROW_3_Y = (int) (Game.HEIGHT * 0.3);
    public static final int TABLE_ROW_4_Y = (int) (Game.HEIGHT * 0.1);
    public static final int EXIT_BUTTON_SIDE = 128;

    public ScoreScreen(Game game, Score score) {
        this.game = game;
        this.score = score;

        hit300 = new Texture(Gdx.files.internal("score/hit300.png"));
        hit100 = new Texture(Gdx.files.internal("score/hit100.png"));
        hit50 = new Texture(Gdx.files.internal("score/hit50.png"));
        hit0 = new Texture(Gdx.files.internal("score/hit0.png"));
        combo = new Texture(Gdx.files.internal("score/combo.png"));
        acc = new Texture(Gdx.files.internal("score/acc.png"));
        scoreSign = new Texture(Gdx.files.internal("score/score.png"));
        backButton = new Texture(Gdx.files.internal("backarrow.png"));
        header = new Texture(Gdx.files.internal("score/header.png"));
        headerSprite = new Sprite(header);
        uiClick = Gdx.audio.newSound(Gdx.files.internal("uiClick.ogg"));

        headerSprite.setSize(Game.WIDTH, (float) Game.HEIGHT / 7);
        headerSprite.setColor(1, 1, 1, 0.7f);
        headerSprite.setCenter((float) Game.WIDTH / 2, Game.HEIGHT - (float) header.getHeight() / 2);

        parameter.size = HEADER_FONT_SIZE;
        headerFont = generator.generateFont(parameter);
        parameter.size = SCORE_FONT_SIZE;
        scoreFont = generator.generateFont(parameter);
        generator.dispose();
        valueOffset = hit300.getWidth() + OFFSET * 4;
        textureHeightOffset = hit0.getHeight() / 2 + OFFSET * 5;

        grade = new Texture(Gdx.files.internal(score.getGradePath()));
    }

    @Override
    public void show() {
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        headerSprite.draw(game.batch);


        headerFont.draw(game.batch, score.getMapName(), OFFSET, Game.HEIGHT - OFFSET);
        headerFont.draw(game.batch, "Played on " + score.getPlayDate() + " " + score.getPlayTime(), OFFSET, Game.HEIGHT - 2 * OFFSET - HEADER_FONT_SIZE);

        //drawing textures
        game.batch.draw(hit300, TABLE_ROW_1_X, TABLE_ROW_1_Y);
        game.batch.draw(hit100, TABLE_ROW_2_X, TABLE_ROW_1_Y);
        game.batch.draw(hit50, TABLE_ROW_1_X, TABLE_ROW_2_Y);
        game.batch.draw(hit0, TABLE_ROW_2_X, TABLE_ROW_2_Y);
        game.batch.draw(combo, TABLE_ROW_1_X, TABLE_ROW_3_Y);
        game.batch.draw(acc, TABLE_ROW_2_X, TABLE_ROW_3_Y);
        game.batch.draw(scoreSign, (float) (TABLE_ROW_1_X + TABLE_ROW_2_X) / 2, TABLE_ROW_4_Y);
        game.batch.draw(grade, (float) (Game.WIDTH * 0.55), (float) Game.HEIGHT / 2 - (float) grade.getHeight() / 2);


        if (Gdx.input.getX() < EXIT_BUTTON_SIDE && Gdx.input.getY() > Game.HEIGHT - EXIT_BUTTON_SIDE) {
            game.batch.draw(backButton, 10, 7, EXIT_BUTTON_SIDE + 25, EXIT_BUTTON_SIDE + 25);

            if (Gdx.input.isTouched()) {
                uiClick.play(0.40f);
                game.setScreen(new MenuScreen(game));
            }
        } else {
            game.batch.draw(backButton, 10, 7, EXIT_BUTTON_SIDE, EXIT_BUTTON_SIDE);
        }

        // drawing values
        scoreFont.draw(game.batch, score.getCount300(), TABLE_ROW_1_X + valueOffset, TABLE_ROW_1_Y + textureHeightOffset);
        scoreFont.draw(game.batch, score.getCount100(), TABLE_ROW_2_X + valueOffset, TABLE_ROW_1_Y + textureHeightOffset);
        scoreFont.draw(game.batch, score.getCount50(), TABLE_ROW_1_X + valueOffset, TABLE_ROW_2_Y + textureHeightOffset);
        scoreFont.draw(game.batch, score.getCount0(), TABLE_ROW_2_X + valueOffset, TABLE_ROW_2_Y + textureHeightOffset);
        scoreFont.draw(game.batch, score.getMaxCombo(), TABLE_ROW_1_X + valueOffset, TABLE_ROW_3_Y + textureHeightOffset);
        scoreFont.draw(game.batch, score.getAccuracy(), TABLE_ROW_2_X + valueOffset, TABLE_ROW_3_Y + textureHeightOffset);
        scoreFont.draw(game.batch, score.getTotalScore(), (float) (TABLE_ROW_1_X + TABLE_ROW_2_X) / 2 + valueOffset + OFFSET * 15, TABLE_ROW_4_Y + (float) scoreSign.getHeight() / 2 + OFFSET * 5);

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
