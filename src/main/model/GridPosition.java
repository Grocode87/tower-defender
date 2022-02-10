package model;


/**
 * Represents a grid position on the grid.
 *
 * The gridX and gridY correlate to the grid position, not an exact x or y position
 */
public class GridPosition {
    private int gridX;
    private int gridY;

    public GridPosition(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
    }

    /**
     * REQUIRES: current GridPosition is on the grid
     * MODIFIES: returns the current GridPosition converted to an exact x,y Position object
     */
    public Position getPosition() {
        return new Position(this.gridX * GridCell.WIDTH, this.gridY * GridCell.HEIGHT);
    }

    /**
     * MODIFIES: this
     * EFFECTS: moves the position in the dx and dy direction on the grid.
     */
    public void move(int dx, int dy) {
        this.gridX += dx;
        this.gridY += dy;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }
}
