package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the Tower Defense Game as a whole,
 * contains the grid, the towers, the wave manager, and the player's assets
 */
public class TDGame {

    public static final int TICKS_PER_SECOND = 60;
    private boolean ended = false;

    private Grid grid;
    private List<Tower> towers = new ArrayList<>();
    private WaveManager waveManager;

    private int money;

    public TDGame(Grid grid, int startMoney) {
        this.grid = new Grid();
        this.money = startMoney;
        this.waveManager = new WaveManager(this);
    }

    /**
     * EFFECTS: updates the waveManager and every tower
     */
    public void tick() {
        this.waveManager.tick();

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

    /**
     * MODIFIES: this
     * EFFECTS: Get the cell at the given GridPosition, if it is a tower cell,
     *          place the tower at that position and remove the cost of the tower from the user's money
     */
    public void placeTower(GridPosition gridPosition) {
        GridCell cell = this.grid.getCellAtPos(gridPosition);
        if (cell == null) {
            return;
        }

        if (cell.getCellType() == Grid.TowerCell) {
            if (Tower.COST <= money) {
                this.towers.add(new Tower(gridPosition, this));
                this.money -= Tower.COST;
                System.out.println("Remaining money: " + money);
            }
        }
    }

    /**
     * MODIFIES: this
     * EFFECTS: adds the amount the user's money
     */
    public void addMoney(int amount) {
        this.money += amount;
    }

    public WaveManager getWaveManager() {
        return waveManager;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public int getMoney() {
        return money;
    }


}
