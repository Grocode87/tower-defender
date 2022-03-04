package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistance.Saveable;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the enemies currently on the screen and manages spawning of enemies for each wave
 */
public class WaveManager implements Saveable {
    public static final int SECONDS_BETWEEN_WAVES = 5;
    public static final int SECONDS_BETWEEN_ENEMIES = 1;

    public static final int BASE_ENEMIES = 2;
    public static final int NEW_ENEMIES_PER_WAVE = 3;

    private TDGame game;
    private int timePassed = 0;

    private int currWave = 0;
    private List<Enemy> enemies = new ArrayList<>();

    private int numEnemiesToSpawn = 0;

    /**
     * REQUIRES: game to be the active game object
     * EFFECTS: Initialize the wave manager and set its game object
     * MODIFIES: this
     * */
    public WaveManager(TDGame game) {
        this.game = game;
    }

    /**
     * EFFECTS: moves every enemy that currently exists, and steps the wave forward
     * MODIFIES: this
     */
    public void tick() {
        for (Enemy e : enemies) {
            e.move();
        }

        stepWave();
    }

    /**
     * EFFECTS: - if the wave has no enemies left to spawn and there are no enemies left, it will start the between wave
     *               state
     *          - if the between wave state is active and enough time has passed, it will start a new wave
     *          - if the wave has more enemies to spawn and the time between enemy spawns has been reached,
     *               it will spawn another enemy
     *
     *          add 1 to timePassed
     * MODIFIES: this
     */
    public void stepWave() {
        if (numEnemiesToSpawn == 0) {
            if (enemies.size() <= 0) {
                if (timePassed >= (SECONDS_BETWEEN_WAVES * TDGame.TICKS_PER_SECOND)) {
                    timePassed = 0;
                    startNewWave();
                } else {
                    timePassed += 1;
                }
            }
        } else {
            if (timePassed >= (SECONDS_BETWEEN_ENEMIES * TDGame.TICKS_PER_SECOND)) {
                spawnEnemy();
                timePassed = 0;
                numEnemiesToSpawn -= 1;
            } else {
                timePassed += 1;
            }
        }
    }

    /**
     * REQUIRES: currWave >= 0
     * MODIFIES: this
     * EFFECTS: increase the currWave by 1, and set the number of enemies to spawn based on the current wave
     */
    public void startNewWave() {
        currWave += 1;

        timePassed = SECONDS_BETWEEN_ENEMIES * TDGame.TICKS_PER_SECOND;
        numEnemiesToSpawn = BASE_ENEMIES + ((currWave - 1) * NEW_ENEMIES_PER_WAVE);
    }

    /**
     * MODIFIES: this
     * EFFECTS: spawn a new enemy at the origin location on the map
     */
    public void spawnEnemy() {
        enemies.add(new Enemy(new Position(0,1), "BAD GUY", this.game, Enemy.SPEED));
    }

    /**
     * REQUIRES: currWave >= 0
     * EFFECTS: return a message to display about the current wave, 1 of 3 options:
     *          - first wave has not started
     *          - a wave is currently in progress
     *          - currently between 2 waves
     */
    public String getWaveMessage() {
        if (currWave == 0) {
            return "Place your first tower and get ready!";
        } else {
            if (numEnemiesToSpawn > 0 ||  enemies.size() > 0) {
                return "Wave " + currWave;
            } else {
                return "Prepare for wave " + (currWave + 1);
            }
        }
    }

    /**
     * REQUIRES: enemies must have a reference to the same TDGame object as the wave manager
     * MODIFIES: this
     * EFFECTS: sets the enemies list to the passed in enemies list. HELPER FOR UNIT TESTS, not used in game
     */
    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public int getCurrWave() {
        return currWave;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public void setTimePassed(int timePassed) {
        this.timePassed = timePassed;
    }

    public void setCurrWave(int currWave) {
        this.currWave = currWave;
    }

    public void setNumEnemiesToSpawn(int numEnemiesToSpawn) {
        this.numEnemiesToSpawn = numEnemiesToSpawn;
    }

    public int getNumEnemiesToSpawn() {
        return numEnemiesToSpawn;
    }

    // EFFECTS: Converts the WaveManager to a JSON Object and returns it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();

        json.put("timePassed", timePassed);
        json.put("currWave", currWave);
        json.put("numEnemiesToSpawn", numEnemiesToSpawn);

        JSONArray enemiesJson = new JSONArray();
        for (Enemy e : enemies) {
            enemiesJson.put(e.toJson());
        }
        json.put("enemies", enemiesJson);

        return json;
    }
}
