package model;

import com.googlecode.lanterna.TextColor;

public class Tower {

    public static final int DAMAGE = 25;
    public static final int RELOAD_TIME_SECONDS = 3;
    public static final int RANGE = 1;
    public static final int COST = 40;

    private int ticksSinceFired = RELOAD_TIME_SECONDS * TDGame.TICKS_PER_SECOND - 5;

    private GridPosition gridPosition;
    private TDGame game;
    private TextColor color = TextColor.ANSI.BLUE;

    public Tower(GridPosition gridPosition, TDGame game) {
        this.gridPosition = gridPosition;
        this.game = game;
    }

    public boolean cellInRange(GridPosition cellPosition) {
        return ((Math.abs(cellPosition.getGridX() - gridPosition.getGridX()) <= RANGE)
                && (Math.abs(cellPosition.getGridY() - gridPosition.getGridY()) <= RANGE));
    }

    public boolean attack() {
        // Go through every enemy in game
        for (Enemy e : game.getWaveManager().getEnemies()) {
            if (cellInRange(e.getPosition().getGridPosition())) {
                e.damage(DAMAGE);
                this.color = TextColor.ANSI.WHITE;
                return true;
            }
        }

        return false;
    }

    public void tick() {
        this.color = TextColor.ANSI.BLUE;

        if (ticksSinceFired >= RELOAD_TIME_SECONDS * TDGame.TICKS_PER_SECOND) {
            if (attack()) {
                ticksSinceFired = 0;
            }
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
}
