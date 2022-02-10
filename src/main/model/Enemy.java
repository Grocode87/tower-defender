package model;

import com.googlecode.lanterna.TextColor;

/**
 * Represents an enemy in the game
 */
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

    /**
     * REQUIRES: position needs to be on a path GridCell, game needs to be the one used by the UI, speed cannot be 0
     * MODIFIES: this
     * EFFECTS: Initializes a new enemy with its position, direction, name, game reference, health, and speed
     */
    public Enemy(Position position, String name, TDGame game, double speed) {
        this.position = position;
        this.direction = Direction.RIGHT;
        this.name = name;
        this.game = game;
        this.health = MAX_HEALTH;
        this.speed = speed;
    }

    /**
     * MODIFIES: this
     * EFFECTS: moves the enemy in the direction that is passed in
     *          returns true if the move is succesful, false if it cannot move in that direction
     *          (can move in that direction if the next gridPosition is a path cell)
     */
    public boolean moveInDirection(Direction moveDirection) {
        GridPosition gridPos = position.getGridPosition();

        GridCell nextCell = this.game.getGrid().getCellAtPos(moveDirection.nextGridPosition(gridPos));

        if (nextCell.getCellType() == Grid.PathCell) {
            this.direction = moveDirection;
            this.position = direction.nextPosition(this.position, speed);
            return true;
        }
        return false;
    }

    /**
     * MODIFIES: this
     * EFFECTS: moves the enemy in the direction that it is currently facing
     *          returns true if the move is succesful, false if it cannot move in that direction
     *          (can move in that direction if the next position is in a path cell)
     */
    public boolean moveInCurrDirection() {
        GridCell nextCell = game.getGrid().getCellAtPos(this.direction.nextPosition(position, speed).getGridPosition());

        if (nextCell.getCellType() == Grid.PathCell) {
            this.direction = direction;
            this.position = direction.nextPosition(this.position, speed);
            return true;
        }

        return false;
    }

    /**
     * MODIFIES: this
     * EFFECTS: either move in current direction or,
     *          if currently moving left or right: try to change to down or up and then move
     *          if currently moving up or down: try to change to left or right and then move
     */
    public void move() {
        if (!moveInCurrDirection()) {
            System.out.println("cant move in direction");
            if (direction == Direction.UP || direction == Direction.DOWN) {
                System.out.println("currently up and down, trying left then right");
                if (!moveInDirection(Direction.LEFT)) {
                    System.out.println("left not work, going right");
                    moveInDirection(Direction.RIGHT);
                }
            } else {
                System.out.println("currently left or right, trying down then up");
                if (!moveInDirection(Direction.DOWN)) {
                    System.out.println("down not work, going up");
                    moveInDirection(Direction.UP);
                }
            }
        }
    }

    /**
     * MODIFIES: this
     * EFFECTS: subtract amount from the enemies health
     *          if the health is less than 0 after the subtraction, set health to 0
     *          change the color of the entity to a shade that represents its current health
     *
     *          if health is 0, add the enemie's kill reward to the user's money and
     *                          remove the enemy
     */
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
