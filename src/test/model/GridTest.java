package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridTest {
    Grid g;

    @BeforeEach
    void setup() {
        g = new Grid();
    }

    @Test
    void testConstructor() {
        assertEquals(g.getGridCells().get(0).getCellType(), 2);
        assertEquals(g.getGridCells().get(10).getCellType(), 1);
    }

    @Test
    void testGetCellAtPosOutOfBounds() {
        assertNull(g.getCellAtPos(new GridPosition(100, 5)));
        assertNull(g.getCellAtPos(new GridPosition(5, 100)));
    }
}
