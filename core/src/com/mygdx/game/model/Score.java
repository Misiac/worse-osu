package com.mygdx.game.model;

public class Score {

    private final String mapName;
    private final String playDate;
    private final String playTime;
    private final String count300;
    private final String count100;
    private final String count50;
    private final String count0;
    private final String maxCombo;
    private final String accuracy;
    private final Grade grade;
    private final String totalScore;
    private final boolean loseFlag;

    public Score(String mapName, String playDate, String playTime, int count300, int count100, int count50, int count0, int maxCombo, long totalScore, String accuracy, boolean loseFlag) {
        this.mapName = mapName;
        this.playDate = playDate;
        this.playTime = playTime;
        this.count300 = String.valueOf(count300);
        this.count100 = String.valueOf(count100);
        this.count50 = String.valueOf(count50);
        this.count0 = String.valueOf(count0);
        this.maxCombo = String.valueOf(maxCombo);
        this.totalScore = String.valueOf(totalScore);
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
            if (count0.equals("0")) return Grade.S;
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
        return count300;
    }

    public String getCount100() {
        return count100;
    }

    public String getCount50() {
        return count50;
    }

    public String getCount0() {
        return count0;
    }

    public String getMaxCombo() {
        return maxCombo;
    }

    public String getGradePath() {
        return grade.path;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public String getAccuracy() {
        return accuracy;
    }
}
