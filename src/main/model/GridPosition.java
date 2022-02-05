package model;

public class GridPosition {
    private int gridX;
    private int gridY;

    public GridPosition(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public Position getPosition() {
        return new Position(this.gridX * GridCell.WIDTH, this.gridY * GridCell.HEIGHT);
    }

    public void move(int dx, int dy) {
        this.gridX += dx;
        this.gridY += dy;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }
}
