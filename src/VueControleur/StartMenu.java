package VueControleur;
import javax.swing.*;

import modele.HighScoreButtonHandler;
import modele.Jeu;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class StartMenu extends JFrame {
    public StartMenu() {
        setTitle("Sokoban Start Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400); // Adjust the size to accomodate new button
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 50)));

        addButton("Start Game", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LevelSelector levelSelector = new LevelSelector();
                levelSelector.setVisible(true);
            }
        });

        add(Box.createRigidArea(new Dimension(0, 30)));

        addButton("View Highscore", new HighScoreButtonHandler());

        add(Box.createRigidArea(new Dimension(0, 30)));

        addButton("Exit", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        setLocationRelativeTo(null);
    }

    private JButton addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(listener);
        add(button);
        return button;
    }

    private String getHighscores() {
        return "Placeholder highscore data";
    }
}