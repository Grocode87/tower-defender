package ui;

import javax.swing.*;
import java.awt.*;

public class SwingMain extends JFrame {

    public SwingMain() {
        SwingGame game = new SwingGame();
        add(game);
        addKeyListener(game);
        addWindowListener(game);

        setResizable(false);
        pack();

        setSize(640, 480);
        setTitle("Tower Defense Game");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            SwingMain ex = new SwingMain();
            ex.setVisible(true);
        });
    }
}
