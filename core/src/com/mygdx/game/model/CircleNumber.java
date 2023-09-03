package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

@SuppressWarnings("ALL")
public enum CircleNumber {


    N0("numbers/default-0.png"),

    N1("numbers/default-1.png"),

    N2("numbers/default-2.png"),

    N3("numbers/default-3.png"),

    N4("numbers/default-4.png"),

    N5("numbers/default-5.png"),

    N6("numbers/default-6.png"),

    N7("numbers/default-7.png"),

    N8("numbers/default-8.png"),

    N9("numbers/default-9.png");

    final String path;
    final Texture texture;


    CircleNumber(String path) {
        this.path = path;
        this.texture = new Texture(Gdx.files.internal(path));
    }

    public String getPath() {
        return path;
    }
    public Texture getTexture() {
        return texture;
    }
}
