package model;

import com.googlecode.lanterna.TextColor;
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
    Position position = new Position(0, 1);
    Enemy e;

    @BeforeEach
    void setup() {
        e = new Enemy(position, "Test", game, 1);
    }

    @Test
    void testConstructor() {
        assertEquals(e.getPosition(), position);
        assertEquals(e.getDirection(), Direction.RIGHT);
        assertEquals(e.getName(), "Test");
        assertEquals(e.getHealth(), Enemy.MAX_HEALTH);
    }

    @Test
    void testMoveInDirectionCanMoveRight() {
        assertTrue(e.moveInDirection(Direction.RIGHT));
    }

    @Test
    void testMoveInDirectionCantMoveRight() {
        for (int i = 0; i < 6 * GridCell.WIDTH; i++) {
            e.move();
        }
        assertTrue(e.moveInCurrDirection());
        assertFalse(e.moveInDirection(Direction.RIGHT));
    }

    @Test
    void testMoveChangeToDown() {
        testMoveInDirectionCantMoveRight();
        e.move();
        assertEquals(e.getDirection(), Direction.DOWN);
    }

    @Test
    void testMoveChangeToLeft() {
        testMoveChangeToDown();
        for (int i = 0; i <= 3 * GridCell.HEIGHT - 1; i++) {
            e.move();
        }
        assertEquals(e.getDirection(), Direction.LEFT);
    }


    @Test
    void testMoveChangeToUp() {
        testMoveChangeToLeft();
        for (int i = 0; i <= 5 * GridCell.WIDTH; i++) {
            e.move();
        }
        assertEquals(e.getDirection(), Direction.UP);
    }

    @Test
    void testMoveChangeToRight() {
        testMoveChangeToUp();
        for (int i = 0; i <= 7 * GridCell.WIDTH; i++) {
            e.move();
        }

        assertEquals(e.getDirection(), Direction.RIGHT);
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
        assertEquals(game.getMoney(), 100 + Enemy.KILL_REWARD);
    }

    @Test
    void testDamageMoreThanAll() {
        e.damage(Enemy.MAX_HEALTH + 1);
        assertEquals(e.getHealth(), 0);
        assertEquals(game.getMoney(), 100 + Enemy.KILL_REWARD);
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
        e.setDirection(Direction.DOWN);
        assertEquals(Direction.DOWN, e.getDirection());
    }


    @Test
    void testSetDirectionLeft() {
        e.setDirection(Direction.LEFT);
        assertEquals(Direction.LEFT, e.getDirection());
    }

    @Test
    void testSetHealth() {
        e.setHealth(69);
        assertEquals(69, e.getHealth());
    }
}
