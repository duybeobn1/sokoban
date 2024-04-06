package modele.sound;

import javax.sound.sampled.*;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.lang.Class;

public class SoundController {
    private Clip backgroundClip; // Variable pour stocker le clip de la musique de fond
    private Map<String, Clip> soundEffects;

    public SoundController() {
        soundEffects = new HashMap<>();
    }
        

    public void playBackgroundMusic(String filePath) {
        try {
            URL url = SoundController.class.getResource(filePath);
            if (url != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                backgroundClip = AudioSystem.getClip(); // Stocker le clip de la musique de fond
                backgroundClip.open(audioInputStream);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            } else {
                System.err.println("Fichier audio introuvable : " + filePath);
            }
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void loadSoundEffect(String filePath) {
        try {
            URL url = SoundController.class.getResource(filePath);
            if (url != null) {
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
                Clip soundClip = AudioSystem.getClip();
                soundClip.open(audioInputStream);
                soundEffects.put(filePath, soundClip);
            } else {
                System.err.println("Fichier audio introuvable : " + filePath);
            }
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    public void playSoundEffect(String filePath) {
        Clip soundClip = soundEffects.get(filePath);
        if (soundClip != null) {
            soundClip.setFramePosition(0); // Rembobiner au début
            soundClip.start();
        } else {
            System.err.println("Effet sonore non chargé : " + filePath);
        }
    }

    public void stopBackgroundMusic() {
        if (backgroundClip != null && backgroundClip.isRunning()) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    public void closeSoundEffects() {
        for (Clip soundClip : soundEffects.values()) {
            soundClip.close();
        }
        soundEffects.clear();
    }
}
