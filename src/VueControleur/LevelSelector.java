package VueControleur;

import modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;

public class LevelSelector extends JFrame {

static int totalTime = 0;
JLabel timerLabel;


    public LevelSelector() {

        setTitle("Level Selector");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 400);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(Box.createRigidArea(new Dimension(0, 50)));
        int gridRows = 6;
        int gridColumns = 5;
        JPanel panel = new JPanel(new GridLayout(gridRows, gridColumns));

        for (int i = 0; i < 20; i++) {
            final int level = i;   
            panel.add(addButton("Level " + (i+1), e -> loadLevel(level)));
        }
        getContentPane().add(panel);

        add(Box.createRigidArea(new Dimension(0, 50)));

        addButton("Back", e -> back());
        timerLabel = new JLabel();
        timerLabel.setMaximumSize(new Dimension(100, 30));
        timerLabel.setFont(new Font("Arial", Font.BOLD, 13));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(this.timerLabel);
        setLocationRelativeTo(null);

    }

    private JButton addButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(350, 100)); 
        button.setFont(new Font("Arial", Font.BOLD, 17)); 
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.addActionListener(listener);
        add(button);
        return button;
    }

    void loadLevel(int level) {
        dispose();
        Jeu jeu = new Jeu(level);
        VueControleur vc = new VueControleur(jeu);
        vc.startAndTrackTime();
        vc.startTrackingPas();
        vc.setVisible(true);
    }

    private void back() {
        
        this.dispose(); // Close the current level selector window
        StartMenu startMenu = new StartMenu(); // Create a new start menu
        startMenu.setVisible(true); // Show the new start menu
    }
}