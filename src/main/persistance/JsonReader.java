package persistance;

import model.*;
import model.direction.Direction;
import model.direction.FacingDirection;
import model.grid.Grid;
import model.position.GridPosition;
import model.position.Position;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Handles reading the JSON game store and creates a new TDGame Object from it
 *
 * Partially based off code from Example Project
 */

public class JsonReader {


    // EFFECTS: reads the game from file and returns it
    // throws IOException if an error occurs while reading the file
    public TDGame read(String source) throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseGame(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses the game from file and returns it
    public TDGame parseGame(JSONObject jsonObject) {
        TDGame game = new TDGame(new Grid(), jsonObject.getInt("money"));
        addTowers(game, jsonObject);
        addWaveManager(game, jsonObject.getJSONObject("waveManager"));
        addBullets(game, jsonObject.getJSONArray("bullets"));
        return game;
    }


    // EFFECTS: parses the towers from the json object and add them to the game
    // MODIFIES: game
    public void addTowers(TDGame game, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("towers");
        ArrayList<Tower> newTowers = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject towerJson = (JSONObject) json;
            JSONObject towerPosJson = towerJson.getJSONObject("gridPos");
            GridPosition towerGridPos = new GridPosition(towerPosJson.getInt("gridX"),
                    towerPosJson.getInt("gridY"));

            Tower newTower = new Tower(towerGridPos, game);
            newTower.setLevel(towerJson.getInt("level"));

            newTowers.add(newTower);
        }

        game.setTowers(newTowers);
    }

    // EFFECTS; parses the wave manager ands it's enemies from the json object and adds them to game
    // MODIFIES: game
    public void addWaveManager(TDGame game, JSONObject jsonObject) {
        WaveManager wm = new WaveManager(game);
        wm.setTimePassed(jsonObject.getInt("timePassed"));
        wm.setCurrWave(jsonObject.getInt("currWave"));
        wm.setNumEnemiesToSpawn(jsonObject.getInt("numEnemiesToSpawn"));

        // add enemies
        JSONArray jsonArray = jsonObject.getJSONArray("enemies");
        ArrayList<Enemy> newEnemies = new ArrayList<>();
        for (Object json : jsonArray) {
            JSONObject enemyJson = (JSONObject) json;
            JSONObject enemyPos = (JSONObject) enemyJson.getJSONObject("pos");
            Position enemyGridPos = new Position(enemyPos.getDouble("posX"), enemyPos.getDouble("posY"));

            Enemy newEnemy = new Enemy(enemyGridPos, enemyJson.getString("name"), game,
                    enemyJson.getDouble("speed"));
            newEnemy.setDirection(FacingDirection.valueOf(enemyJson.getString("facingDirection")));
            newEnemy.setHealth(enemyJson.getInt("health"));
            newEnemies.add(newEnemy);
        }
        wm.setEnemies(newEnemies);
        game.setWaveManager(wm);
    }

    public void addBullets(TDGame game, JSONArray jsonArray) {
        for (Object json : jsonArray) {
            JSONObject bulletJson = (JSONObject) json;
            JSONObject bulletPos = (JSONObject) bulletJson.getJSONObject("pos");
            JSONObject bulletDir = (JSONObject) bulletJson.getJSONObject("direction");
            Position pos = new Position(bulletPos.getDouble("posX"), bulletPos.getDouble("posY"));
            Direction dir = new Direction(bulletDir.getDouble("dx"), bulletDir.getDouble("dx"));

            Bullet newBullet = new Bullet(pos, dir, bulletJson.getDouble("rotation"),
                    bulletJson.getInt("power"));
            game.addBullet(newBullet);
        }
    }
}
