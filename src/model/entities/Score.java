package model.entities;

import java.io.Serializable;

public class Score implements Serializable {
    private int score;
    private String name;

    public Score (int score, String name) {
        this.score = score;
        this.name = name;
    }

    public Score() {}

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " -------- " + score;
    }


}
