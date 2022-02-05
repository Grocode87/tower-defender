package model;

import com.googlecode.lanterna.TextColor;

public class Enemy {
    public static final int MAX_HEALTH = 100;
    public static final double SPEED = 0.05;
    public static final int KILL_REWARD = 20;

    private Position position;
    private Direction direction;
    private int health;
    private double speed;
    private String name;

    private TDGame game;
    private TextColor.RGB color = new TextColor.RGB(252, 215, 3);

    public Enemy(Position position, String name, TDGame game, double speed) {
        this.position = position;
        this.direction = Direction.RIGHT;
        this.name = name;
        this.game = game;
        this.health = MAX_HEALTH;
        this.speed = speed;
    }

    public boolean moveInDirection(Direction moveDirection) {
        GridPosition gridPos = position.getGridPosition();

        GridCell nextCell = game.getGrid().getCellAtPos(moveDirection.nextPosition(position, speed).getGridPosition());

        if (nextCell.getCellType() == Grid.PathCell) {
            this.direction = moveDirection;
            this.position = direction.nextPosition(this.position, speed);
            return true;
        }
        return false;
    }

    public void move() {
        if (!moveInDirection(direction)) {
            if (direction == Direction.UP || direction == Direction.DOWN) {

                if (!moveInDirection(Direction.LEFT)) {
                    moveInDirection(Direction.RIGHT);
                }
            } else {
                if (!moveInDirection(Direction.DOWN)) {
                    moveInDirection(Direction.UP);
                }
            }
        }
    }

    public void damage(int amount) {
        this.health -= amount;
        if (health < 0) {
            health = 0;
        }
        this.color = new TextColor.RGB(255, (255 * this.health) / MAX_HEALTH, 0);

        if (this.health <= 0) {
            game.addMoney(Enemy.KILL_REWARD);
            game.getWaveManager().getEnemies().remove(this);
        }
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    public TextColor.RGB getColor() {
        return color;
    }

}
