package model.entities;

import model.enums.Direction;

public class PacMan extends Character {
    private int spawnR;
    private int spawnC;
    private Direction spawnDirection;


    public int getSpawnR() {
        return spawnR;
    }


    public void setSpawnR(int spawnR) {
        this.spawnR = spawnR;
    }


    public int getSpawnC() {
        return spawnC;
    }

    public void setSpawnC(int spawnC) {
        this.spawnC = spawnC;
    }

    public Direction getSpawnDirection() {
        return spawnDirection;
    }

    public void setSpawnDirection(Direction spawnDirection) {
        this.spawnDirection = spawnDirection;
    }
}
