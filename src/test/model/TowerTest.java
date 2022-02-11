package model;

import com.googlecode.lanterna.TextColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TowerTest {

    Grid grid = new Grid();
    TDGame game1 = new TDGame(grid, 100);
    TDGame game2 = new TDGame(grid, 100);
    Tower t;
    Tower t2;

    @BeforeEach
    void setup() {
        ArrayList<Enemy> testEnemies = new ArrayList<>();
        testEnemies.add(new Enemy(new Position(0,1), "Test", game2, 1));
        testEnemies.add(new Enemy(new Position(1,1), "Test 2", game2, 1));

        game2.getWaveManager().setEnemies(testEnemies);

        t = new Tower(new GridPosition(1,2), game1);
        t2 = new Tower(new GridPosition(1,2), game2);
    }

    @Test
    void testCellInRange() {
        assertTrue(t.cellInRange(new GridPosition(0,1)));
        assertTrue(t.cellInRange(new GridPosition(1,1)));
        assertTrue(t.cellInRange(new GridPosition(2,1)));

        assertFalse(t.cellInRange(new GridPosition(3,1)));
        assertFalse(t.cellInRange(new GridPosition(1,4)));
    }

    @Test
    void testAttackNothingInRange() {
        assertFalse(t.attack());
    }

    @Test
    void testAttackEntityNotInRange() {
        // move first enemy in game2 out of range
        for (int i = 0; i < 4 * GridCell.WIDTH; i++) {
            game2.getWaveManager().getEnemies().get(0).move();
        }
        assertTrue(t2.attack());
    }

    @Test
    void testAttackEntityInRange() {
        assertTrue(t2.attack());

        assertEquals(game2.getWaveManager().getEnemies().get(0).getHealth(), Enemy.MAX_HEALTH - Tower.DAMAGE);
    }

    @Test
    void testAttackColorChangeToWhite() {
        t2.attack();
        assertEquals(t2.getColor(), TextColor.ANSI.WHITE);
    }

    @Test
    void testTickColorChangeBackToBlue() {
        testAttackColorChangeToWhite();
        t2.tick();
        t2.tick();

        assertEquals(t2.getColor(), TextColor.ANSI.BLUE);
    }

    @Test
    void testTickNotFireWhileReloading() {
        // Get first fire done
        t2.tick();

        t2.tick();
        t2.tick();

        assertEquals(t2.getTicksSinceFired(), 2);
    }

    @Test
    void testTickFireAfterReload() {

        t2.tick();

        for (int i = 0; i <= Tower.RELOAD_TIME_SECONDS * TDGame.TICKS_PER_SECOND; i++) {
            t2.tick();
        }

        assertEquals(t2.getTicksSinceFired(), 0);
    }

    @Test
    void testGetGridPosition() {
        assertEquals(t.getGridPosition().getGridX(), 1);
        assertEquals(t.getGridPosition().getGridY(), 2);
    }
}
