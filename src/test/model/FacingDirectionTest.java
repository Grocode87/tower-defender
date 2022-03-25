package model;

import model.direction.FacingDirection;
import model.position.Position;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Represents 1 of 4 facing directions - UP, DOWN, LEFT, RIGHT
 *
 * Partially based off code from provided examples
 */
public class FacingDirectionTest {
    FacingDirection up;
    FacingDirection down;
    FacingDirection left;
    FacingDirection right;

    Position position;

    @BeforeEach
    void setup() {
        up = FacingDirection.UP;
        down = FacingDirection.DOWN;
        left = FacingDirection.LEFT;
        right = FacingDirection.RIGHT;

        position = new Position(1.0, 1.0);
    }

    @Test
    void testNextPositionHorizontal() {
        assertEquals(left.nextPosition(position, 1).getPosX(), position.getPosX() - 1);
        assertEquals(right.nextPosition(position, 1).getPosX(), position.getPosX() + 1);
    }

    @Test
    void testNextPositionVertical() {
        assertEquals(up.nextPosition(position, 1).getPosY(), position.getPosY() - 1);
        assertEquals(down.nextPosition(position, 1).getPosY(), position.getPosY() + 1);

    }

    @Test
    void testToJson() {
        JSONObject testObj = new JSONObject();
        testObj.put("direction", up.name());

        assertEquals(testObj.get("direction"), up.toJson().get("direction"));
    }
}
