package model.grid;

import model.position.GridPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game grid
 * */
public class Grid {

    public static final int WIDTH = 15;
    public static final int HEIGHT = 11;

    // 3 different types of cells
    public static final int EmptyCell = 0;
    public static final int PathCell = 1;
    public static final int TowerCell = 2;

    private static int[][] mapLayout = {
            {2,2,2,2,2,2,2,2,2,2,2,2,2,0,0},
            {1,1,2,1,1,1,1,1,1,1,1,1,2,0,0},
            {2,1,2,1,2,2,2,2,2,2,2,1,2,0,0},
            {2,1,2,1,2,1,1,1,1,1,2,1,2,0,0},
            {2,1,2,1,2,1,2,2,2,1,2,1,2,0,0},
            {2,1,2,1,2,1,2,1,1,1,2,1,2,0,0},
            {2,1,2,1,2,1,2,1,2,2,2,1,2,0,0},
            {2,1,2,1,1,1,2,1,1,1,2,1,2,0,0},
            {2,1,2,2,2,2,2,2,2,1,2,1,2,2,2},
            {2,1,1,1,1,1,1,1,1,1,2,1,1,1,1},
            {2,2,2,2,2,2,2,2,2,2,2,2,2,2,2}};
    
    private final List<GridCell> gridCells = new ArrayList<>();

    /**
     * MODIFIES: this
     * EFFECTS: creates a new grid, and creates the grid cells based on the default mapLayout variable
     */
    public Grid() {
        for (int i = 0; i < mapLayout.length; i++) {
            for (int j = 0; j < mapLayout[i].length; j++) {
                gridCells.add(new GridCell(mapLayout[i][j], new GridPosition(j, i)));
            }
        }
    }

    /**
     * REQUIRES: mapLayout must be a 2d int array with equal width and height, and each item must be 0, 1, or 2.
     * MODIFIES: this
     * EFFECTS: creates a new grid, and creates the grid cells based on the passed in mapLayout variable
     */
    public Grid(int[][] mapLayout) {
        this.mapLayout = mapLayout;
        for (int i = 0; i < mapLayout.length; i++) {
            for (int j = 0; j < mapLayout[i].length; j++) {
                gridCells.add(new GridCell(mapLayout[i][j], new GridPosition(j, i)));
            }
        }
    }

    /**
     * EFFECTS: return the cell at a given grid position
     *          if there is no cell at given position, return null
     */
    public GridCell getCellAtPos(GridPosition pos) {
        if (pos.getGridX() >= 0 && pos.getGridX() < mapLayout[0].length) {
            if (pos.getGridY() >= 0 && pos.getGridY() < mapLayout.length) {
                return gridCells.get(pos.getGridY() * (mapLayout[0].length) + pos.getGridX());
            }
        }

        return null;
    }

    public List<GridCell> getGridCells() {
        return this.gridCells;
    }

}
