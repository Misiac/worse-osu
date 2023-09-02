package com.mygdx.game;

import com.badlogic.gdx.InputProcessor;

public class ScrollProcessor implements InputProcessor {

    private float effectVolume = 0.1f;
    private float musicVolume = 0.1f;
    public static final float JUMP = 0.05f;

    public float getEffectVolume() {
        return effectVolume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (amountY == -1.0) {
            if (effectVolume <= 1 || musicVolume <= 1) {
                effectVolume += JUMP;
                musicVolume += JUMP;
            }
        } else if (amountY == 1.0) {
            if (effectVolume >= 0 || musicVolume >= 0) {
                effectVolume -= JUMP;
                musicVolume -= JUMP;
                if (effectVolume - JUMP < 0 || musicVolume - JUMP < 0) {
                    effectVolume = 0;
                    musicVolume = 0;
                }
            }
        }

        return false;
    }
}
