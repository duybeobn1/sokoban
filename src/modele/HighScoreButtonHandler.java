package modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HighScoreButtonHandler implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String highscores = readHighscoresFromFile("res/highscores.txt");
            JOptionPane.showMessageDialog(null, highscores, "Highscores", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to read highscores from file.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String readHighscoresFromFile(String filename) throws IOException {
        StringBuilder highscores = new StringBuilder();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
        while ((line = reader.readLine()) != null) {
            highscores.append(line).append("\n");
        }
        reader.close();
        return highscores.toString();
    }
    
}
