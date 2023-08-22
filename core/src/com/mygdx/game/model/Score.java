package com.mygdx.game.model;

public class Score {

    // change the types in constructor instead of getters
    private String mapName;
    private String playDate;
    private String playTime;
    private int count300;
    private int count100;
    private int count50;
    private int count0;
    private int maxCombo;
    private String accuracy;
    private Grade grade;
    private long totalScore;
    private boolean loseFlag;

    public Score(String mapName, String playDate, String playTime, int count300, int count100, int count50, int count0, int maxCombo, long totalScore, String accuracy, boolean loseFlag) {
        this.mapName = mapName;
        this.playDate = playDate;
        this.playTime = playTime;
        this.count300 = count300;
        this.count100 = count100;
        this.count50 = count50;
        this.count0 = count0;
        this.maxCombo = maxCombo;
        this.totalScore = totalScore;
        this.accuracy = accuracy;
        this.loseFlag = loseFlag;
        grade = calculateGrade();
    }

    private Grade calculateGrade() {

        if (loseFlag) return Grade.X;
        double acc = Double.parseDouble(accuracy.substring(0, 4));
        if (acc < 50) return Grade.D;
        else if (acc < 70) return Grade.C;
        else if (acc < 80) return Grade.B;
        else if (acc < 90) return Grade.A;
        else {
            if (count0 == 0) return Grade.S;
            else return Grade.A;
        }
    }


    @Override
    public String toString() {
        return "Score{" +
                "mapName='" + mapName + '\'' +
                ", playDate='" + playDate + '\'' +
                ", playTime='" + playTime + '\'' +
                ", count300=" + count300 +
                ", count100=" + count100 +
                ", count50=" + count50 +
                ", count0=" + count0 +
                ", maxCombo=" + maxCombo +
                ", grade='" + grade + '\'' +
                ", totalScore=" + totalScore +
                '}';
    }

    public String getMapName() {
        return mapName;
    }

    public String getPlayDate() {
        return playDate;
    }

    public String getPlayTime() {
        return playTime;
    }

    public String getCount300() {
        return String.valueOf(count300);
    }

    public String getCount100() {
        return String.valueOf(count100);
    }

    public String getCount50() {
        return String.valueOf(count50);
    }

    public String getCount0() {
        return String.valueOf(count0);
    }

    public String getMaxCombo() {
        return String.valueOf(maxCombo);
    }

    public String getGradePath() {
        return grade.path;
    }

    public String getTotalScore() {
        return String.valueOf(totalScore);
    }

    public String getAccuracy() {
//        System.out.println(accuracy);
        return String.valueOf(accuracy);
    }
}
