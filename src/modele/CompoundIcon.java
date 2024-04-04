package modele;

import javax.swing.*;
import java.awt.*;

public class CompoundIcon implements Icon {
    private Icon background;
    private Icon foreground;

    public CompoundIcon(Icon background, Icon foreground) {
        this.background = background;
        this.foreground = foreground;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (background != null) {
            background.paintIcon(c, g, x, y);
        }
        if (foreground != null) {
            foreground.paintIcon(c, g, x, y);
        }
    }

    @Override
    public int getIconWidth() {
        return Math.max(background.getIconWidth(), foreground.getIconWidth());
    }

    @Override
    public int getIconHeight() {
        return Math.max(background.getIconHeight(), foreground.getIconHeight());
    }
}
