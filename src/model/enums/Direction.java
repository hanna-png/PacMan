
package model.enums;
public enum Direction {
    UP(-1,0),
    DOWN(1,0),
    LEFT(0,-1),
    RIGHT(0,1);

    public final int directionRow, directionColumn;

    Direction(int directionRow, int directionColumn){
        this.directionRow =directionRow; this.directionColumn = directionColumn;
    }
}