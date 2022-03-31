package model;

import javafx.geometry.Point2D;
import javafx.scene.shape.Circle;
import model.grid.Grid;
import model.grid.GridCell;
import model.position.GridPosition;
import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Saveable;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents the Tower Defense Game as a whole,
 * contains the grid, the towers, the wave manager, and the player's assets
 */
public class TDGame implements Saveable {

    public static final int TICKS_PER_SECOND = 60;
    private boolean ended = false;

    private Grid grid;
    private List<Tower> towers = new ArrayList<>();
    private WaveManager waveManager;

    private List<Bullet> bullets = new ArrayList<>();

    private int money;

    /**
     * REQUIRES: startMoney must be greater than 0
     */
    public TDGame(Grid grid, int startMoney) {
        this.grid = grid;
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

        for (Bullet b: bullets) {
            b.tick();
        }
        checkBulletCollision();
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

                EventLog.getInstance().logEvent(new Event("Tower added at (X:"
                        + gridPosition.getGridX() + ", Y:"
                        + gridPosition.getGridY() + ")"));
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Remove the tower at the gridPosition and refund the money for the tower,
    //          if there is no tower at gridPosition, do nothing
    public void removeTower(GridPosition gridPosition) {
        Iterator<Tower> towerIter = towers.iterator();
        while (towerIter.hasNext()) {
            Tower t = towerIter.next();
            if (t.getGridPosition().getGridX() == gridPosition.getGridX()
                    && t.getGridPosition().getGridY() == gridPosition.getGridY()) {
                towerIter.remove();

                EventLog.getInstance().logEvent(new Event("Tower removed at (X:"
                        + t.getGridPosition().getGridX() + ", Y:"
                        + t.getGridPosition().getGridY() + ")"));
                this.money += Tower.COST;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Upgrade the tower at the gridPosition if the player has enough money,
    //           subtract the cost from the player's money
    public void upgradeTower(GridPosition gridPosition) {
        for (Tower t : towers) {
            if (t.getGridPosition().getGridX() == gridPosition.getGridX()
                    && t.getGridPosition().getGridY() == gridPosition.getGridY()) {
                if (money >= Tower.UPGRADE_COST) {
                    this.money -= Tower.UPGRADE_COST;
                    t.upgrade();


                }
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: Add the bullet to the game
    public void addBullet(Bullet b) {
        bullets.add(b);
    }


    // MODIFIES: this
    // EFFECTS: for every enemy - checks enemy/bullet collisions
    //            if collision: remove the bullet and damage the enemy, remove enemy if health <= 0
    public void checkBulletCollision() {
        Iterator<Enemy> enemyIter = waveManager.getEnemies().iterator();
        while (enemyIter.hasNext()) {
            Enemy e = enemyIter.next();
            Iterator<Bullet> bulletIter = bullets.iterator();

            while (bulletIter.hasNext()) {
                Bullet b = bulletIter.next();

                Circle enemyCircle = new Circle(e.getPosition().getPosX() + Enemy.RADIUS,
                        e.getPosition().getPosY() + Enemy.RADIUS,
                        Enemy.RADIUS);

                Point2D point = new Point2D((int) b.getPos().getPosX() + (10 / 2),
                        (int) b.getPos().getPosY() + (5 / 2));

                if (enemyCircle.contains(point)) {
                    bulletIter.remove();

                    e.damage(b.getPower());
                    if (e.getHealth() <= 0) {
                        e.kill();
                        enemyIter.remove();
                    }
                }
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

    public List<Bullet> getBullets() {
        return bullets;
    }

    public void setTowers(List<Tower> towers) {
        this.towers = towers;
    }

    public void setWaveManager(WaveManager waveManager) {
        this.waveManager = waveManager;
    }

    // EFFECTS: Converts the TDGame to a JSON Object and returns it
    @Override
    public JSONObject toJson() {
        JSONObject jsonGame = new JSONObject();
        jsonGame.put("money", money);

        // Add towers
        JSONArray jsonTowers = new JSONArray();
        for (Tower t : towers) {
            jsonTowers.put(t.toJson());
        }
        jsonGame.put("towers", jsonTowers);

        // Add WaveManager
        JSONObject jsonWaveManager = waveManager.toJson();
        jsonGame.put("waveManager", jsonWaveManager);

        // Add bullets
        JSONArray jsonBullets = new JSONArray();
        for (Bullet b : bullets) {
            jsonBullets.put(b.toJson());
        }
        jsonGame.put("bullets", jsonBullets);

        return jsonGame;
    }
}
