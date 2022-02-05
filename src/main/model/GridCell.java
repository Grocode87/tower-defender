package model;

public class GridCell {
    public static final int WIDTH = 3;
    public static final int HEIGHT = 1;

    private int cellType;
    private GridPosition gridPosition;


    public GridCell(int cellType, GridPosition gridPosition) {
        this.cellType = cellType;
        this.gridPosition = gridPosition;
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }

    public int getCellType() {
        return cellType;
    }
}
