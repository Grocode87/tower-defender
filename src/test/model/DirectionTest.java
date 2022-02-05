package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {
    Direction up;
    Direction down;
    Direction left;
    Direction right;

    Position position;

    @BeforeEach
    void setup() {
        up = Direction.UP;
        down = Direction.DOWN;
        left = Direction.LEFT;
        right = Direction.RIGHT;

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
}
