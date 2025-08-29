package model.entities;

import model.enums.Direction;

public abstract class Character {
    private int currentR;
    private int currentC;
    private Direction currentDirection;
    private volatile int currentFrame;
    private volatile int speed;




    public synchronized void changeFrame(){
        currentFrame = (currentFrame == 1) ? 2 : 1;
    }


    public synchronized int  getCurrentR() {
        return currentR;
    }

    public synchronized void setCurrentR(int currentR) {
        this.currentR = currentR;
    }

    public synchronized int getCurrentC() {
        return currentC;
    }

    public synchronized void setCurrentC(int currentC) {
        this.currentC = currentC;
    }

    public synchronized Direction getCurrentDirection() {
        return currentDirection;
    }

    public synchronized void setCurrentDirection(Direction currentDirection) {
        this.currentDirection = currentDirection;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int  getSpeed() {
        return speed;
    }

    public synchronized void setSpeed(int speed) {
        this.speed = speed;
    }
}
