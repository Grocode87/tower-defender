package model;

import com.googlecode.lanterna.TextColor;
import model.direction.FacingDirection;
import model.grid.Grid;
import model.grid.GridCell;
import model.position.GridPosition;
import model.position.Position;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {

    private static final int[][] mapLayout = {
            {2,2,2,2,2,2,2,0,0,0},
            {1,1,1,1,1,1,2,0,0,0},
            {2,2,2,2,2,1,2,0,0,0},
            {2,1,1,1,2,1,2,0,0,0},
            {2,1,2,2,2,1,2,0,0,0},
            {2,1,1,2,2,1,2,0,0,0},
            {2,2,1,1,1,1,2,0,0,0},
            {0,2,2,2,2,2,2,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0},
            {0,0,0,0,0,0,0,0,0,0}};

    Grid grid = new Grid(mapLayout);
    TDGame game = new TDGame(grid, 100);
    Position position;
    Enemy e;

    @BeforeEach
    void setup() {
        GridPosition startGridPos = new GridPosition(0, 1);

        double startX = startGridPos.getPosition().getPosX();
        double startY = (startGridPos.getPosition().getPosY() + GridCell.HEIGHT) / 2 + Enemy.RADIUS;
        System.out.println(startX);
        position = new Position(startX, startY);
        e = new Enemy(position, "Test", game, 1);
        e.setDirection(FacingDirection.RIGHT);
    }

    @Test
    void testConstructor() {
        assertEquals(e.getPosition(), position);
        assertEquals(e.getDirection(), FacingDirection.RIGHT);
        assertEquals(e.getName(), "Test");
        assertEquals(e.getHealth(), Enemy.MAX_HEALTH);
    }

    @Test
    void testMoveInDirectionCanMoveRight() {
        assertTrue(e.moveInDirection(FacingDirection.RIGHT));
    }

    @Test
    void testMoveInDirectionCantMoveRight() {
        for (int i = 0; i < (6 * GridCell.WIDTH); i++) {
            e.move();
        }

        assertTrue(e.moveInCurrDirection());
        assertFalse(e.moveInDirection(FacingDirection.RIGHT));
    }

    @Test
    void testMoveChangeToDown() {
        testMoveInDirectionCantMoveRight();
        e.move();
        e.move();
        assertEquals(FacingDirection.DOWN, e.getDirection());
    }

    @Test
    void testMoveChangeToLeft() {
        testMoveChangeToDown();
        for (int i = 0; i <= 6 * GridCell.HEIGHT; i++) {
            e.move();
        }
        assertEquals(FacingDirection.LEFT, e.getDirection());
    }


    @Test
    void testMoveChangeToUp() {
        testMoveChangeToLeft();
        for (int i = 0; i <= 2 * GridCell.WIDTH; i++) {
            e.move();
        }
        assertEquals(e.getDirection(), FacingDirection.UP);
    }

    @Test
    void testMoveChangeToRight() {
        testMoveChangeToUp();
        for (int i = 0; i <= 9 * GridCell.WIDTH; i++) {
            e.move();
        }

        assertEquals(e.getDirection(), FacingDirection.RIGHT);
    }

    @Test
    void testDamageColorChange() {
        e.damage(0);
        assertEquals(e.getColor().getGreen(), new TextColor.RGB(255, 255, 0).getGreen());
    }
    @Test
    void testDamageNotAll() {
        e.damage(Enemy.MAX_HEALTH - 1);
        assertEquals(e.getHealth(), 1);
    }

    @Test
    void testDamageAll() {
        e.damage(Enemy.MAX_HEALTH);
        assertEquals(e.getHealth(), 0);
    }

    @Test
    void testDamageMoreThanAll() {
        e.damage(Enemy.MAX_HEALTH + 1);
        assertEquals(e.getHealth(), 0);
    }

    @Test
    void testKill() {
        int startMoney = game.getMoney();
        e.kill();
        assertEquals(startMoney + Enemy.KILL_REWARD, game.getMoney());
    }

    @Test
    void testToJson() {
        JSONObject testObj = new JSONObject();
        testObj.put("direction", e.getDirection());
        testObj.put("pos", e.getPosition());
        testObj.put("name", e.getName());

        assertEquals(testObj.get("name"), e.toJson().get("name"));
    }

    @Test
    void testSetDirectionDown() {
        e.setDirection(FacingDirection.DOWN);
        assertEquals(FacingDirection.DOWN, e.getDirection());
    }


    @Test
    void testSetDirectionLeft() {
        e.setDirection(FacingDirection.LEFT);
        assertEquals(FacingDirection.LEFT, e.getDirection());
    }

    @Test
    void testSetHealth() {
        e.setHealth(69);
        assertEquals(69, e.getHealth());
    }
}
