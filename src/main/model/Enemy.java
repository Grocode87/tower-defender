package model;

import com.googlecode.lanterna.TextColor;

public class Enemy {
    public static final int MAX_HEALTH = 100;
    public static final double SPEED = 0.05;

    private Position position;
    private Direction direction;
    private int health;
    private String name;

    private TDGame game;
    private TextColor.RGB color = new TextColor.RGB(252, 215, 3);

    public Enemy(Position position, String name, TDGame game) {
        this.position = position;
        this.direction = Direction.RIGHT;
        this.name = name;
        this.game = game;
        this.health = MAX_HEALTH;
    }

    private boolean moveInDirection(Direction moveDirection) {
        GridPosition gridPos = position.getGridPosition();

        GridCell nextCell = game.getGrid().getCellAtPos(moveDirection.nextPosition(position, SPEED).getGridPosition());

        if (nextCell.getCellType() == Grid.PathCell) {
            this.direction = moveDirection;
            this.position = direction.nextPosition(this.position, SPEED);
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

        this.color = new TextColor.RGB(255, (250 * this.health) / MAX_HEALTH, 0);

        if (this.health <= 0) {
            game.getEnemies().remove(this);
        }
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
