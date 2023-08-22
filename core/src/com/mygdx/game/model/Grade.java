package com.mygdx.game.model;

public enum Grade {


    S("score/rankingS.png"),

    A("score/rankingA.png"),

    B("score/rankingB.png"),

    C("score/rankingC.png"),

    D("score/rankingD.png"),

    X("score/rankingX.png");

    String path;


    Grade(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

}
