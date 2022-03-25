package model;

import model.grid.GridCell;
import model.position.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    public Position p;

    @BeforeEach
    void setup() {
        p = new Position(1, GridCell.HEIGHT + 2);
    }

    @Test
    void testConstructor() {
        assertEquals(p.getPosX(), 1);
        assertEquals(p.getPosY(), GridCell.HEIGHT + 2);
    }

    @Test
    void testGetGridPosition() {
        assertEquals(p.getGridPosition().getGridX(), 0);
        assertEquals(p.getGridPosition().getGridY(), 1);
    }


}
