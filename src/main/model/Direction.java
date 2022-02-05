package model;

/**
 * Represents a facing direction
 * in game
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
     * Moves a position one increment
     * in the facing direction
     */
    public Position nextPosition(Position pos, double speed) {
        return new Position(
                pos.getPosX() + (double)dx * speed,
                pos.getPosY() + (double)dy * speed
        );
    }


}
