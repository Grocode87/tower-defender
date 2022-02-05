package model;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EnemyTest {

    Grid grid = new Grid();
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
        e.setPosition(new Position(8 * GridCell.WIDTH - 1, 1));
        assertFalse(e.moveInDirection(Direction.RIGHT));
    }

    @Test
    void testMoveChangeToDown() {
        e.setPosition(new Position(8 * GridCell.WIDTH - 1, 1));
        e.move();
        assertEquals(e.getDirection(), Direction.DOWN);
        e.move();
        e.move();
        e.move();
        e.move();
        assertEquals(e.getDirection(), Direction.LEFT);

    }

    @Test
    void testMoveChangeToLeft() {
        e.setPosition(new Position(8 * GridCell.WIDTH - 1, 3 * GridCell.HEIGHT - 1));
        e.move();
        e.move();
        e.move();
        assertEquals(e.getDirection(), Direction.LEFT);
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
}
