package Controlleur;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.w3c.dom.events.Event;
public class Keyboard implements KeyListener {
    private boolean[] keys;
    public static boolean UP, LEFT, RIGHT, DOWN;

    public Keyboard() {
        keys = new boolean[256];
        UP = false;
        LEFT = false;
        RIGHT = false;
        DOWN = false;
    }

    public void update() {
        UP = keys[KeyEvent.VK_UP];
        LEFT = keys[KeyEvent.VK_LEFT];
        RIGHT = keys[KeyEvent.VK_RIGHT];
        DOWN = keys[KeyEvent.VK_DOWN];
    }
    @Override
    public void keyTyped(KeyEvent e) {
        //unused
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
}
