package model;

import java.util.ArrayList;
import java.util.List;

public class WaveManager {
    // Create waves
    public static final int SECONDS_BETWEEN_WAVES = 5;
    public static final int SECONDS_BETWEEN_ENEMIES = 1;

    public static final int BASE_ENEMIES = 2;
    public static final int NEW_ENEMIES_PER_WAVE = 3;

    private TDGame game;
    private int timePassed = 0;

    private int currWave = 0;
    private List<Enemy> enemies = new ArrayList<>();

    private int numEnemiesToSpawn = 0;


    public WaveManager(TDGame game) {
        this.game = game;
    }

    public void tick() {
        for (Enemy e : enemies) {
            e.move();
        }

        stepWave();
    }

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

    public void startNewWave() {
        currWave += 1;
        System.out.println("Starting Wave: " + currWave);
        timePassed = SECONDS_BETWEEN_ENEMIES * TDGame.TICKS_PER_SECOND;
        numEnemiesToSpawn = BASE_ENEMIES + ((currWave - 1) * NEW_ENEMIES_PER_WAVE);
    }

    public void spawnEnemy() {
        enemies.add(new Enemy(new Position(0,1), "BAD GUY", this.game, Enemy.SPEED));
    }

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

    public int getCurrWave() {
        return currWave;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }



    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public int getNumEnemiesToSpawn() {
        return numEnemiesToSpawn;
    }
}
