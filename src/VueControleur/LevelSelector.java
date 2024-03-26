package VueControleur;

import modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelSelector extends JFrame {
    public LevelSelector() {
        setTitle("Level Selector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 50)));

        // Add Level-select buttons
        addButton("Level 0", e-> loadLevel(0));
        addButton("Level 1", e -> loadLevel(1));
        addButton("Level 2", e -> loadLevel(2));
        addButton("Level 3", e -> loadLevel(3));

        add(Box.createRigidArea(new Dimension(0, 50)));

        // Add back button
        addButton("Back", e -> back());

        setLocationRelativeTo(null);
    }

    private JButton addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(400, 50));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(listener);
        add(button);
        return button;
    }

    private void loadLevel(int level) {
        // load the selected level
        dispose();
        Jeu jeu = new Jeu(level);
        VueControleur vc = new VueControleur(jeu);
        vc.setVisible(true);
    }

    private void back() {
        this.dispose(); // Close the current level selector window
        StartMenu startMenu = new StartMenu(); // Create a new start menu
        startMenu.setVisible(true); // Show the new start menu
    }
}