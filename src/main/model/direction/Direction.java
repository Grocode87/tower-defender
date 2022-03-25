package model.direction;

import model.position.Position;
import org.json.JSONObject;
import persistance.Saveable;

/**
 * Represents a facing direction in game
 *
 * Based off code from the SnakeConsole example project
 */
public class Direction implements Saveable {

    private double dx;
    private double dy;

    /**
     * REQUIRES: at least one of dx or dy needs to be 0, as the direction cannot be horizontal
     * MODIFIES: this
     * EFFECTS: initializes a new direction with its horizontal and vertical direction
     */
    public Direction(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
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



    // EFFECTS: Converts the FacingDirection to a JSON Object and returns it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("dx", dx);
        json.put("dy", dy);

        return json;
    }
}
