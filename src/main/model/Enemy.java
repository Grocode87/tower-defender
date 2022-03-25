package model;

import model.direction.FacingDirection;
import model.grid.Grid;
import model.grid.GridCell;
import model.position.GridPosition;
import model.position.Position;
import org.json.JSONObject;
import persistance.Saveable;

import java.awt.*;

/**
 * Represents an enemy in the game
 */
public class Enemy implements Saveable {
    public static final int MAX_HEALTH = 100;
    public static final double SPEED = 2;
    public static final int KILL_REWARD = 20;

    public static final int RADIUS = 10;

    private Position position;
    private FacingDirection facingDirection;
    private int health;
    private double speed;
    private String name;

    private TDGame game;
    private Color color = new Color(255, 255, 0);


    /**
     * REQUIRES: position needs to be on a path GridCell, game needs to be the one used by the UI, speed cannot be 0
     * MODIFIES: this
     * EFFECTS: Initializes a new enemy with its position, facingDirection, name, game reference, health, and speed
     */
    public Enemy(Position position, String name, TDGame game, double speed) {
        this.position = position;
        this.facingDirection = FacingDirection.RIGHT;
        this.name = name;
        this.game = game;
        this.health = MAX_HEALTH;
        this.speed = speed;
    }

    /**
     * MODIFIES: this
     * EFFECTS: moves the enemy in the facingDirection that is passed in
     *          returns true if the move is succesful, false if it cannot move in that facingDirection
     *          (can move in that facingDirection if the next gridPosition is a path cell)
     */
    public boolean moveInDirection(FacingDirection moveDirection) {
        GridPosition gridPos = position.getGridPosition();

        GridCell nextCell = this.game.getGrid().getCellAtPos(moveDirection.nextGridPosition(gridPos));

        if (nextCell.getCellType() == Grid.PathCell) {
            this.facingDirection = moveDirection;
            this.position = facingDirection.nextPosition(this.position, speed);
            return true;
        }
        return false;
    }

    // if move forward (see if currX + 1/2 grid cell + dx) is a path cell
            // if not try down
    /**
     * MODIFIES: this
     * EFFECTS: moves the enemy in the facingDirection that it is currently facing
     *          returns true if the move is succesful, false if it cannot move in that facingDirection
     *          (can move in that facingDirection if the next position is in a path cell)
     */
    public boolean moveInCurrDirection() {
        Position nextPos = facingDirection.nextPosition(position, speed);

        // Calculate the amounts to multiply the x and y offset by to get to the end of the current grid for a cell
        // 1 if moving in positive facingDirection
        // 0 if moving in negative facingDirection
        int dirOffsetX = ((facingDirection.getDx() + 1) / 2);
        int dirOffsetY = ((facingDirection.getDy() + 1) / 2);

        // Calculate the amount to offset the position by get to the end of the cell if the current pos is in
        // the center of the current square
        int cellWidthOffsetX = ((GridCell.WIDTH / 2)) * dirOffsetX + (RADIUS * facingDirection.getDx());
        int cellWidthOffsetY = ((GridCell.HEIGHT / 2)) * dirOffsetY + (RADIUS * facingDirection.getDy());

        Position nextCellPos = new Position(nextPos.getPosX() + cellWidthOffsetX,
                nextPos.getPosY() + cellWidthOffsetY);
        GridCell nextCell = game.getGrid().getCellAtPos(nextCellPos.getGridPosition());

        if (nextCell.getCellType() == Grid.PathCell) {
            this.position = facingDirection.nextPosition(this.position, speed);
            return true;
        }

        return false;
    }

    /**
     * MODIFIES: this
     * EFFECTS: either move in current facingDirection or,
     *          if currently moving left or right: try to change to down or up and then move
     *          if currently moving up or down: try to change to left or right and then move
     */
    public void move() {
        if (!moveInCurrDirection()) {
            if (facingDirection == FacingDirection.UP || facingDirection == FacingDirection.DOWN) {
                if (!moveInDirection(FacingDirection.LEFT)) {
                    moveInDirection(FacingDirection.RIGHT);
                }
            } else {
                if (!moveInDirection(FacingDirection.DOWN)) {
                    moveInDirection(FacingDirection.UP);
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
        this.color = new Color(255, (255 * this.health) / MAX_HEALTH, 0);
    }

    public void kill() {
        game.addMoney(Enemy.KILL_REWARD);
    }

    public Position getPosition() {
        return position;
    }

    public FacingDirection getDirection() {
        return facingDirection;
    }

    public int getHealth() {
        return health;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public double getSpeed() {
        return speed;
    }

    public void setDirection(FacingDirection facingDirection) {
        this.facingDirection = facingDirection;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    // EFFECTS: Converts the Enemy to a JSON Object and returns it
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("pos", position.toJson());
        json.put("facingDirection", facingDirection.name());
        json.put("health", health);
        json.put("speed", speed);
        json.put("name", name);
        json.put("color", color.toString());

        return json;
    }
}
