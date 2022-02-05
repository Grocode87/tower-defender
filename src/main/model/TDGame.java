package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Tower Defense Game as a whole
 */
public class TDGame {

    public static final int TICKS_PER_SECOND = 60;
    private boolean ended = false;
    private Grid grid;
    private List<Enemy> enemies = new ArrayList<>();
    private List<Tower> towers = new ArrayList<>();

    public TDGame(Grid grid) {
        this.grid = new Grid();
        this.enemies.add(new Enemy(new Position(0,1), "BAD GUY", this));
    }

    public void tick() {
        for (Enemy e : enemies) {
            e.move();
        }

        for (Tower t : towers) {
            t.tick();
        }
    }

    public boolean isEnded() {
        return ended;
    }

    public Grid getGrid() {
        return grid;
    }

    public void placeTower(GridPosition gridPosition) {
        GridCell cell = this.grid.getCellAtPos(gridPosition);
        if (cell == null) {
            return;
        }

        if (cell.getCellType() == Grid.TowerCell) {
            this.towers.add(new Tower(gridPosition, this));
        }
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Tower> getTowers() {
        return towers;
    }
}
