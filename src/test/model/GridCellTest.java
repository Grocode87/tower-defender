package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GridCellTest {

    GridCell emptyCell;
    GridCell pathCell;
    GridCell towerCell;

    GridPosition gridPos = new GridPosition(1,1);

    @BeforeEach
    void setup() {
        emptyCell = new GridCell(0, gridPos);
        pathCell = new GridCell(1, gridPos);
        towerCell = new GridCell(2, gridPos);
    }

    @Test
    void testGetCellType() {
        assertEquals(emptyCell.getCellType(), 0);
        assertEquals(pathCell.getCellType(), 1);
        assertEquals(towerCell.getCellType(), 2);
    }

    @Test
    void testGetGridPos() {
        assertEquals(emptyCell.getGridPosition(), gridPos);
    }
}
