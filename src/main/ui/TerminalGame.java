package ui;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogBuilder;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import model.*;
import persistance.JsonReader;
import persistance.JsonWriter;

import javax.xml.soap.Text;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TerminalGame {
    private TDGame game;
    private Screen screen;
    private WindowBasedTextGUI endGui;
    private char square = '█';

    private GridPosition cursor = new GridPosition(0, 0);

    private JsonReader jsonReader = new JsonReader();
    private JsonWriter jsonWriter = new JsonWriter();

    /**
     * Begins the game and method does not leave execution
     * until game is complete.
     */
    public void start() throws IOException, InterruptedException {
        screen = new DefaultTerminalFactory().createScreen();
        screen.startScreen();

        TerminalSize terminalSize = screen.getTerminalSize();

        game = new TDGame(new Grid(), 150);

        beginTicks();
    }

    /**
     * Begins the game cycle. Ticks once every Game.TICKS_PER_SECOND until
     * game has ended and the endGui has been exited.
     */
    private void beginTicks() throws IOException, InterruptedException {
        while (!game.isEnded() || endGui.getActiveWindow() != null) {
            tick();
            Thread.sleep(1000L / TDGame.TICKS_PER_SECOND);
        }

        System.exit(0);
    }

    /**
     * Handles one cycle in the game by taking user input,
     * ticking the game internally, and rendering the effects
     */
    private void tick() throws IOException {
        handleUserInput();

        game.tick();

        screen.setCursorPosition(new TerminalPosition(0, 0));
        screen.clear();
        render();
        screen.refresh();

        screen.setCursorPosition(new TerminalPosition(screen.getTerminalSize().getColumns() - 1, 0));
    }

    /**
     * Sets the snake's direction corresponding to the
     * user's keystroke
     */
    private void handleUserInput() throws IOException {
        KeyStroke stroke = screen.pollInput();

        if (stroke == null) {
            return;
        }

        if (stroke.getCharacter() != null) {
            switch (stroke.getCharacter()) {
                case 'p':
                    game.placeTower(new GridPosition(cursor.getGridX(), cursor.getGridY()));
                    break;
                case 's':
                    saveGame();
                    break;
                case 'l':
                    loadGame();
                    break;
            }
        }

        moveCursor(stroke.getKeyType());
    }


    private void moveCursor(KeyType keyType) {
        switch (keyType) {
            case ArrowUp:
                cursor.move(0, -1);
                break;
            case ArrowDown:
                cursor.move(0, 1);
                break;
            case ArrowRight:
                cursor.move(1, 0);
                break;
            case ArrowLeft:
                cursor.move(-1, 0);
                break;

        }
    }


    /**
     * Renders the current screen.
     * Draws the end screen if the game has ended, otherwise
     * draws the score, snake, and food.
     */
    private void render() {
        if (game.isEnded()) {
            if (endGui == null) {
                drawEndScreen();
            }

            return;
        }

        drawGrid();
        drawCursor();
        drawEnemies();
        drawTowers();
        drawGUI();
    }

    private void drawEndScreen() {
        endGui = new MultiWindowTextGUI(screen);

        new MessageDialogBuilder()
                .setTitle("Game over!")
                .setText("You finished with a score of " + 0 + "!")
                .addButton(MessageDialogButton.Close)
                .build()
                .showDialog(endGui);
    }

    private void drawGrid() {
        for (GridCell cell : game.getGrid().getGridCells()) {
            TextColor tc = TextColor.ANSI.GREEN;
            if (cell.getCellType() == 1) {
                tc = TextColor.ANSI.BLACK;
            } else if (cell.getCellType() == 2) {
                tc = TextColor.ANSI.GREEN;
            }
            drawGridPosition(cell.getGridPosition(), tc);
        }
    }

    private void drawCursor() {
        drawGridPosition(cursor, TextColor.ANSI.WHITE);
    }

    private void drawEnemies() {
        for (Enemy e : game.getWaveManager().getEnemies()) {
            //drawGridPosition(e.getPosition().getGridPosition(), TextColor.ANSI.WHITE);
            drawPosition(e.getPosition(), GridCell.WIDTH, GridCell.HEIGHT, e.getColor());
        }
    }

    private void drawTowers() {
        for (Tower t : game.getTowers()) {
            drawGridPosition(t.getGridPosition(),
                    t.getColor());
        }
    }

    private void drawGUI() {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(TextColor.ANSI.WHITE);


        text.putString(0, 1, game.getWaveManager().getWaveMessage() + "   |   Money: " + game.getMoney());
    }

    private void drawPosition(Position position, int width, int height, TextColor color) {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(color);


        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                text.putString(
                        (int) position.getPosX(),
                        (int) position.getPosY() + 2,
                        String.valueOf(square));
            }
        }
    }

    private void drawGridPosition(GridPosition gridPosition, TextColor color) {
        TextGraphics text = screen.newTextGraphics();
        text.setForegroundColor(color);


        for (int w = 0; w < GridCell.WIDTH; w++) {
            for (int h = 0; h < GridCell.HEIGHT; h++) {
                text.putString(
                        gridPosition.getGridX() * GridCell.WIDTH + w,
                        gridPosition.getGridY() * GridCell.HEIGHT + h + 2,
                        String.valueOf(square));
            }
        }
    }

    /**
     * EFFECTS: Attempts to save the current game to file
     */
    private void saveGame() {
        try {
            jsonWriter.open("./data/gameStore.json");
            jsonWriter.write(game);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file");
        }
    }

    /**
     * EFFECTS: Attempts to set the game to a game loaded from file
     * MODIFIES: this
     */
    private void loadGame() {
        try {
            this.game = jsonReader.read("./data/gameStore.json");
        } catch (IOException e) {
            System.out.println("Unable to read from file");
            System.out.println("Unable to read from file");
        }
    }
}