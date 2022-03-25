package model.direction;

import model.position.GridPosition;
import model.position.Position;
import org.json.JSONObject;
import persistance.Saveable;

/**
 * Represents a facing direction in game
 *
 * Based off code from the SnakeConsole example project
 */
public enum FacingDirection implements Saveable {


    UP(0, -1),
    DOWN(0, 1),
    RIGHT(1, 0),
    LEFT(-1, 0);

    private int dx;
    private int dy;

    /**
     * REQUIRES: at least one of dx or dy needs to be 0, as the direction cannot be horizontal
     * MODIFIES: this
     * EFFECTS: initializes a new direction with its horizontal and vertical direction
     */
    FacingDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }


    /**
     * REQUIRES: speed is >= 0
     * EFFECTS: applies the dx and dy to the passed in position,
     * dx and dy are multiplied by speed.
     */
    public Position nextPosition(Position pos, double speed) {
        return new Position(
                pos.getPosX() + (double) dx * speed,
                pos.getPosY() + (double) dy * speed
        );
    }

    /**
     * EFFECTS: applies the dx and dy to the passed in gridPosition
     */
    public GridPosition nextGridPosition(GridPosition gridPos) {
        return new GridPosition(
                gridPos.getGridX() + dx,
                gridPos.getGridY() + dy
        );
    }



    // EFFECTS: Converts the FacingDirection to a JSON Object and returns it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("direction", this.name());

        return json;
    }
}
