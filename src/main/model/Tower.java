package model;

import com.googlecode.lanterna.TextColor;
import org.json.JSONObject;
import persistance.Saveable;

/**
 * Represents an individual tower in the game
 */
public class Tower implements Saveable {

    public static final int DAMAGE = 25;
    public static final int RELOAD_TIME_SECONDS = 3;
    public static final int RANGE = 1;
    public static final int COST = 40;

    private int ticksSinceFired = RELOAD_TIME_SECONDS * TDGame.TICKS_PER_SECOND;

    private GridPosition gridPosition;
    private TDGame game;
    private TextColor color = TextColor.ANSI.BLUE;

    public Tower(GridPosition gridPosition, TDGame game) {
        this.gridPosition = gridPosition;
        this.game = game;
    }

    /**
     * REQUIRES: cellPosition to be a valid location on the Grid
     * EFFECTS: returns true if the cellPosition is within RANGE number of grid cells in the x and y direction
     */
    public boolean cellInRange(GridPosition cellPosition) {
        return ((Math.abs(cellPosition.getGridX() - gridPosition.getGridX()) <= RANGE)
                && (Math.abs(cellPosition.getGridY() - gridPosition.getGridY()) <= RANGE));
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
                e.damage(DAMAGE);
                ticksSinceFired = 0;
                this.color = TextColor.ANSI.WHITE;
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
        this.color = TextColor.ANSI.BLUE;

        if (ticksSinceFired >= RELOAD_TIME_SECONDS * TDGame.TICKS_PER_SECOND) {
            attack();
        } else {
            ticksSinceFired += 1;
        }
    }

    public GridPosition getGridPosition() {
        return gridPosition;
    }

    public TextColor getColor() {
        return color;
    }

    public int getTicksSinceFired() {
        return ticksSinceFired;
    }



    // EFFECTS: Converts the Tower to a JSON Object and returns it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("gridPos", gridPosition.toJson());
        json.put("color", color.toString());
        json.put("ticksSinceFired", ticksSinceFired);

        return json;
    }
}
