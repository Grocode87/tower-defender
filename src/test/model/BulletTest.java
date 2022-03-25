package model;

import model.direction.Direction;
import model.position.Position;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BulletTest {

    Bullet b;

    @BeforeEach
    void setup() {
        b = new Bullet(new Position(10, 10), new Direction(1, 1), Math.PI / 2, 5);
    }

    @Test
    void testConstructor() {
        assertEquals(10, b.getPos().getPosX());
        assertEquals(10, b.getPos().getPosY());

        assertEquals(1, b.getDir().getDx());
        assertEquals(1, b.getDir().getDy());

        assertEquals(Math.PI / 2, b.getRotation());
        assertEquals(5, b.getPower());
    }

    @Test
    void testToJson() {
        JSONObject bulletJson = b.toJson();

        assertEquals(bulletJson.get("rotation"), b.getRotation());
        assertEquals(bulletJson.get("power"), b.getPower());
    }
}
