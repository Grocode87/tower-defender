package ui;

import model.*;
import model.grid.Grid;
import model.grid.GridCell;
import model.position.GridPosition;
import model.position.Position;
import persistance.JsonReader;
import persistance.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SwingGame extends JPanel implements KeyListener, MouseListener, ActionListener, WindowListener {
    private TDGame game;
    private Timer timer;

    private GridPosition cursor = new GridPosition(0, 0);


    private JsonReader jsonReader = new JsonReader();
    private JsonWriter jsonWriter = new JsonWriter();


    public SwingGame() {
        addMouseListener(this);
        setBackground(Color.WHITE);

        game = new TDGame(new Grid(), 150);
        timer = new Timer(16, this);
        timer.start();

        openStartDialog();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintGame(g);

        //Toolkit.getDefaultToolkit().sync();
    }

    private void paintGame(Graphics g) {

        paintGrid(g);
        paintCursor(g);
        paintEnemies(g);

        paintBullets(g);

        paintTowers(g);
        paintUI(g);

    }

    private void paintGrid(Graphics g) {
        for (GridCell cell : game.getGrid().getGridCells()) {
            g.setColor(Color.GREEN);

            if (cell.getCellType() == 1) {
                g.setColor(Color.BLACK);
            }

            drawRectAtGridPosition(g, cell.getGridPosition());

        }

    }

    private void paintCursor(Graphics g) {
        g.setColor(new Color(255, 255, 255, 150));

        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Point windowLocation = this.getLocationOnScreen();

        drawRectAtGridPosition(g, new Position(mouseLocation.getX() - windowLocation.getX(),
                mouseLocation.getY() - windowLocation.getY()).getGridPosition());
    }

    private void paintEnemies(Graphics g) {
        for (Enemy e : game.getWaveManager().getEnemies()) {
            g.setColor(e.getColor());
            g.fillOval((int) e.getPosition().getPosX(), (int) e.getPosition().getPosY(),
                    Enemy.RADIUS * 2, Enemy.RADIUS * 2);
        }
    }

    private void paintTowers(Graphics g) {
        for (Tower t : game.getTowers()) {
            g.setColor(t.getColor());
            g.fillRect((int) t.getGridPosition().getPosition().getPosX() + 7,
                    (int) t.getGridPosition().getPosition().getPosY() + 7,
                    GridCell.WIDTH - 14, GridCell.HEIGHT - 14);
        }
    }

    private void paintBullets(Graphics g) {
        for (Bullet b : game.getBullets()) {
            Graphics2D gg = (Graphics2D) g.create();
            gg.setColor(Color.cyan);
            gg.rotate(b.getRotation(), b.getPos().getPosX() + 10 / 2, b.getPos().getPosY() + 5 / 2);
            gg.fillRect((int) b.getPos().getPosX(), (int) b.getPos().getPosY(), 10, 5);
            gg.dispose();
        }
    }

    private void paintUI(Graphics g) {
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, this.getWidth(), 30);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.white);
        g2d.drawString(game.getWaveManager().getWaveMessage(), 10, 20);
        g2d.drawString("$" + game.getMoney(), this.getWidth() - 100, 20);
    }

    private void drawRectAtGridPosition(Graphics g, GridPosition gp) {
        g.fillRect((int) gp.getPosition().getPosX(),
                (int) gp.getPosition().getPosY(),
                GridCell.WIDTH, GridCell.HEIGHT);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        game.tick();

        repaint();
    }

    // EFFECTS: Try to save the current state of the game to file
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

    private void openStartDialog() {
        String[] promptButtons = {"Yes","No"};
        int promptResult = JOptionPane.showOptionDialog(null,
                "Do you want to load in your previous game?", "Welcome!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                promptButtons,promptButtons[1]);
        if (promptResult == 0) {
            loadGame();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_S:
                saveGame();
            case KeyEvent.VK_L:
                loadGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            game.placeTower(new Position(e.getX(), e.getY()).getGridPosition());
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            game.removeTower(new Position(e.getX(), e.getY()).getGridPosition());
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            game.upgradeTower(new Position(e.getX(), e.getY()).getGridPosition());
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        String[] promptButtons = {"Yes","No"};
        int promptResult = JOptionPane.showOptionDialog(null,
                "Do you want to save before exiting?", "Closing",
                JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null,
                promptButtons,promptButtons[1]);
        if (promptResult == 0) {
            saveGame();
            System.exit(0);
        } else {
            System.exit(0);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
