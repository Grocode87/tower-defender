package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridPositionTest {

    GridPosition gridPos;

    @BeforeEach
    void setup() {
        gridPos = new GridPosition(2, 2);
    }

    @Test
    void testGetPosition() {
        Position p = gridPos.getPosition();

        assertEquals(p.getPosX(), 2 * GridCell.WIDTH);
        assertEquals(p.getPosY(), 2 * GridCell.HEIGHT);
    }

    @Test
    void testMove() {
        gridPos.move(1, 1);
        assertEquals(gridPos.getGridX(), 2 + 1);
        assertEquals(gridPos.getGridY(), 2 + 1);
    }
}
