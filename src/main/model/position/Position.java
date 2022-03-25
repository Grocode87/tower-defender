package model.position;

import model.grid.GridCell;
import model.position.GridPosition;
import org.json.JSONObject;
import persistance.Saveable;

/**
 * Represents a position in the game space.
 *
 * THis is an exact x, y position and does not relate to the game grid.
 */
public class Position implements Saveable {
    private double posX;
    private double posY;

    /**
     * REQUIRES: posX and posY must be valid pixel locations on the grid
     * MODIFIES: this
     * EFFECTS: creates a new Position object at the given x and y values
     * */
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

    // EFFECTS: Converts the Position to a JSON Object and returns it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("posX", posX);
        json.put("posY", posY);

        return json;
    }
}
