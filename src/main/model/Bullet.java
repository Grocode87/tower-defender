package model;

import model.direction.Direction;
import model.position.Position;
import org.json.JSONObject;
import persistance.Saveable;

import java.awt.*;

/**
 * Class that represents a bullet in the game and its position, direction, rotation, and damage power
 */
public class Bullet implements Saveable {
    public static final int SPEED = 3;

    private Position pos;
    private Direction dir;
    private double rotation;
    private int power;

    public Bullet(Position startPos, Direction dir, double rotation, int power) {
        this.pos = startPos;
        this.dir = dir;
        this.rotation = rotation;
        this.power = power;
    }

    public void tick() {
        this.pos = dir.nextPosition(pos, SPEED);
    }

    public double getRotation() {
        return rotation;
    }

    public Position getPos() {
        return pos;
    }

    public Direction getDir() {
        return dir;
    }

    public int getPower() {
        return power;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("pos", pos.toJson());
        json.put("direction", dir.toJson());
        json.put("rotation", rotation);
        json.put("power", power);

        return json;
    }
}
