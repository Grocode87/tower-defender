package model;

/**
 * Represents a position in the game space.
 *
 * THis is an exact x, y position and does not relate to the game grid.
 */
public class Position {
    private double posX;
    private double posY;

    public Position(double posX, double posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * REQUIRES: position is currently on the grid
     * EFFECTS: returns the grid position of the current x,y position
     */
    public GridPosition getGridPosition() {
        return new GridPosition(
                ((int) this.posX / GridCell.WIDTH),
                ((int) this.posY / GridCell.HEIGHT));
    }

    public double getPosX() {
        return posX;
    }

    public double getPosY() {
        return posY;
    }
}
