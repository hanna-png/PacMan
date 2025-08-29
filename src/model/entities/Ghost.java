package model.entities;

import model.enums.GhostColor;

public class Ghost extends Character {
    private GhostColor ghostColor;

    public Ghost(GhostColor color) {
        this.ghostColor = color;
    }

    public GhostColor getGhostColor() {
        return ghostColor;
    }

    public void setGhostColor(GhostColor ghostColor) {
        this.ghostColor = ghostColor;
    }
}
