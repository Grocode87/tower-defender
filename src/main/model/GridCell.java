package model;

/**
 * Represents a specific cell on the grid
 */
public class GridCell {
    public static final int WIDTH = 3;
    public static final int HEIGHT = 1;

    private int cellType;
    private GridPosition gridPosition;


    /**
     * REQUIRES: cellType must be either 0, 1, or 2
     * MODIFIES: this
     * EFFECTS: creates a new grid cell of the given type and at the given grid position
     * */
    public GridCell(int cellType, GridPosition gridPosition) {
        this.cellType = cellType;
        this.gridPosition = gridPosition;
    }


    public int getCellType() {
        return cellType;
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }
}
