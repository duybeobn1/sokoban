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
        int gridRows = 6;
        int gridColumns = 5;
        JPanel panel = new JPanel(new GridLayout(gridRows, gridColumns));

        // Add Level-select buttons
        for (int i = 0; i < 30; i++) {
            final int level = i;   // Final copy of i to use inside lambda
            panel.add(addButton("Level " + (i+1), e -> loadLevel(level)));
        }
        getContentPane().add(panel);

        add(Box.createRigidArea(new Dimension(0, 50)));

        // Add back button
        addButton("Back", e -> back());

        setLocationRelativeTo(null);
    }

    private JButton addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(100, 30)); // Modify button size
        button.setFont(new Font("Arial", Font.BOLD, 13)); // Modify font size
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