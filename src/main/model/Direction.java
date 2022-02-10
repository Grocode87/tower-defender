package model;

/**
 * Represents a facing direction in game
 *
 * Based off code from the SnakeConsole example project
 */
public enum Direction {
    UP(0, -1),
    DOWN(0, 1),
    RIGHT(1, 0),
    LEFT(-1, 0);

    private int dx;
    private int dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }


    /**
     * REQUIRES: speed is >= 0
     * EFFECTS: applies the dx and dy to the passed in position,
     * dx and dy are multiplied by speed.
     */
    public Position nextPosition(Position pos, double speed) {
        return new Position(
                pos.getPosX() + (double) dx * speed,
                pos.getPosY() + (double) dy * speed
        );
    }

    /**
     * EFFECTS: applies the dx and dy to the passed in gridPosition
     */
    public GridPosition nextGridPosition(GridPosition gridPos) {
        return new GridPosition(
                gridPos.getGridX() + dx,
                gridPos.getGridY() + dy
        );
    }


}
