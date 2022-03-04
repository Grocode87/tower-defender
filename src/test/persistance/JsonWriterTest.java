package persistance;

import model.Grid;
import model.GridPosition;
import model.TDGame;
import model.WaveManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            JsonWriter writer = new JsonWriter();
            writer.open("./data/my\0illegal:fileName.json");
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyGame() {
        try {
            TDGame game = new TDGame(new Grid(), 150);
            JsonWriter writer = new JsonWriter();
            writer.open("./data/testWriterEmptyGame.json");
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader();
            game = reader.read("./data/testWriterEmptyGame.json");
            assertEquals(150, game.getMoney());
            assertEquals(0, game.getWaveManager().getEnemies().size());
            assertEquals(0, game.getWaveManager().getCurrWave());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralGame() {
        try {
            TDGame game = new TDGame(new Grid(), 150);
            game.placeTower(new GridPosition(2, 0));
            game.placeTower(new GridPosition(4, 0));

            for (int i = 0; i < (WaveManager.SECONDS_BETWEEN_WAVES + 1) * TDGame.TICKS_PER_SECOND + 10; i++) {
                game.tick();
            }
            game.tick();
            JsonWriter writer = new JsonWriter();
            writer.open("./data/testWriterGeneralGame.json");
            writer.write(game);
            writer.close();

            JsonReader reader = new JsonReader();
            game = reader.read("./data/testWriterGeneralGame.json");
            assertEquals(70, game.getMoney());
            assertEquals(2, game.getTowers().size());
            assertEquals(2, game.getWaveManager().getEnemies().size());
            assertEquals(75, game.getWaveManager().getEnemies().get(0).getHealth());
            assertEquals(1, game.getWaveManager().getCurrWave());


        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
