package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    public Position p;

    @BeforeEach
    void setup() {
        p = new Position(1, 1);
    }

    @Test
    void testConstructor() {
        assertEquals(p.getPosX(), 1);
        assertEquals(p.getPosY(), 1);
    }

    @Test
    void testGetGridPosition() {
        assertEquals(p.getGridPosition().getGridX(), 0);
        assertEquals(p.getGridPosition().getGridY(), 1);
    }


}
