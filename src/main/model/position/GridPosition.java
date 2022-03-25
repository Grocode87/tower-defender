package model.position;


import model.grid.GridCell;
import org.json.JSONObject;
import persistance.Saveable;

/**
 * Represents a grid position on the grid.
 *
 * The gridX and gridY correlate to the grid position, not an exact x or y position
 */
public class GridPosition implements Saveable {
    private int gridX;
    private int gridY;

    /**
     * REQUIRES: gridX and gridY must be valid locations on the Grid
     * MODIFIES: this
     * EFFECTS: creates a new GridPosition object at the given x and y values
     * */
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

    // EFFECTS: Converts the GridPosition to a JSON Object and returns it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("gridX", gridX);
        json.put("gridY", gridY);

        return json;
    }
}
