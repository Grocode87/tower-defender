package model;

import model.direction.Direction;
import model.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Represents a direction in the game with a dx and dy - double values
 */
public class DirectionTest {
    Direction d;

    @BeforeEach
    void setup() {
        d = new Direction(0.5, 2.5);
    }

    @Test
    void testConstructor() {
        assertEquals(0.5, d.getDx());
        assertEquals(2.5, d.getDy());
    }

    @Test
    void testNextPosition() {
        Position nextPos = d.nextPosition(new Position(1, 1), 2);
        assertEquals(1 + (0.5 * 2), nextPos.getPosX());
        assertEquals(1 + (2.5 * 2), nextPos.getPosY());
    }
}
