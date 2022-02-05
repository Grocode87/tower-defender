package model;

/**
 * Represents a position in the game space.
 */
public class Position {
    private double posX;
    private double posY;

    public Position(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public GridPosition getGridPosition() {
        return new GridPosition(
                ((int)(this.posX / GridCell.WIDTH)),
                ((int)(this.posY / GridCell.HEIGHT)));
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}
