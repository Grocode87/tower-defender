package model;

import com.googlecode.lanterna.TextColor;
import model.direction.Direction;
import model.grid.GridCell;
import model.position.GridPosition;
import model.position.Position;
import org.json.JSONObject;
import persistance.Saveable;

import java.awt.*;

/**
 * Represents an individual tower in the game
 */
public class Tower implements Saveable {

    public static final int DAMAGE = 25;
    public static final int RELOAD_TIME_SECONDS = 1;
    public static final int RANGE = 1;
    public static final int COST = 40;
    public static final int UPGRADE_COST = 100;

    private int ticksSinceFired;

    private GridPosition gridPosition;
    private TDGame game;

    private int level;
    private Color color;

    public Tower(GridPosition gridPosition, TDGame game) {
        this.gridPosition = gridPosition;
        this.game = game;

        this.level = 0;
        this.color = Color.blue;

        ticksSinceFired = RELOAD_TIME_SECONDS * TDGame.TICKS_PER_SECOND;
    }

    /**
     * REQUIRES: cellPosition to be a valid location on the Grid
     * EFFECTS: returns true if the cellPosition is within RANGE number of grid cells in the x and y direction
     */
    public boolean cellInRange(GridPosition cellPosition) {
        return ((Math.abs(cellPosition.getGridX() - gridPosition.getGridX()) <= RANGE)
                && (Math.abs(cellPosition.getGridY() - gridPosition.getGridY()) <= RANGE));
    }

    public Position findTargetPosition(Enemy e) {
        Enemy targetEnemy = new Enemy(e.getPosition(), e.getName(), game, e.getSpeed());
        targetEnemy.setDirection(e.getDirection());

        for (int i = 0; i < 5; i++) {
            targetEnemy.move();
        }

        return targetEnemy.getPosition();
    }


    public void fireBullet(Position target, double targetSpeed) {
        Position towerCenter = new Position(gridPosition.getPosition().getPosX() + (GridCell.WIDTH / 2),
                gridPosition.getPosition().getPosY() + (GridCell.HEIGHT / 2));

        //calculate dx and dy
        double dx = target.getPosX() - towerCenter.getPosX();
        double dy = target.getPosY() - towerCenter.getPosY();

        double rotation = Math.atan2(dy, dx);

        dx = Math.cos(rotation) * Bullet.SPEED;
        dy = Math.sin(rotation) * Bullet.SPEED;

        Bullet bullet = new Bullet(towerCenter, new Direction(dx, dy), rotation, DAMAGE * (level + 1));
        game.addBullet(bullet);
    }

    /**
     * MODIFIES: this
     * EFFECTS: Goes through every enemy in the waveManager,
     *          If the enemy is within range:
     *              damage the enemy
     *              reset attack cooldown
     *              set the tower color to white
     */
    public boolean attack() {
        // Go through every enemy in game
        for (Enemy e : game.getWaveManager().getEnemies()) {
            if (cellInRange(e.getPosition().getGridPosition())) {
                fireBullet(findTargetPosition(e), Enemy.SPEED);
                ticksSinceFired = 0;
                return true;
            }
        }

        return false;
    }

    /**
     * MODIFIES: this
     * EFFECTS: Sets the color of the tower to blue,
     *          checks if enough ticks have passed for the tower to be able to fire again
     *              if yes, tower will attack
     *              if no, number of ticks since last fired is increased by 1
     *
     */
    public void tick() {
        if (ticksSinceFired >= RELOAD_TIME_SECONDS * TDGame.TICKS_PER_SECOND) {
            attack();
        } else {
            ticksSinceFired += 1;
        }
    }

    // MODIFIES: this
    // REQUIRES: current level is 0
    // EFFECTS: upgrades the tower from level 0 to level 1 and changes it's color
    public void upgrade() {
        this.level = 1;
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }

    // REQUIRES: level is either 0 or 1
    // EFFECTS: returns the color of the tower based on it's level
    public Color getColor() {
        if (level == 0) {
            return Color.blue;
        } else {
            return Color.pink;
        }
    }

    public int getTicksSinceFired() {
        return ticksSinceFired;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    // EFFECTS: Converts the Tower to a JSON Object and returns it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("gridPos", gridPosition.toJson());
        json.put("color", color.toString());
        json.put("ticksSinceFired", ticksSinceFired);
        json.put("level", level);

        return json;
    }
}
