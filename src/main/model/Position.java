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
        System.out.println("current position: " + this.posX + ", " + this.posY);
        System.out.println("current gridPosition: " + Math.round(this.posX / GridCell.WIDTH) + ", " + Math.round(this.posY / GridCell.HEIGHT));
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
