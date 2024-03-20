package modele;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import modele.Level;

public class Assets {

	public static Image playerLeft, playerBack, playerRight, PlayerFront;

	public static Image floor, floor2, wall, boxOn, boxOff, spot, outline, outline2;

	public static Font font48;
	public static Font font30;
	public static Font font22;

	public static void init() {
		playerLeft = loadImage("../res/player/left.png").getScaledInstance(Level.TILESIZE, Level.TILESIZE,
				BufferedImage.SCALE_DEFAULT);
		playerBack = loadImage("../res/player/back.png").getScaledInstance(Level.TILESIZE, Level.TILESIZE,
				BufferedImage.SCALE_DEFAULT);
		PlayerFront = loadImage("../res/player/front.png").getScaledInstance(Level.TILESIZE, Level.TILESIZE,
				BufferedImage.SCALE_DEFAULT);
		playerRight = loadImage("../res/player/right.png").getScaledInstance(Level.TILESIZE, Level.TILESIZE,
				BufferedImage.SCALE_DEFAULT);

		floor = loadImage("../res/blocks/ground.png").getScaledInstance(Level.TILESIZE, Level.TILESIZE,
				BufferedImage.SCALE_DEFAULT);
		floor2 = loadImage("../res/blocks/ground2.png").getScaledInstance(Level.TILESIZE, Level.TILESIZE,
				BufferedImage.SCALE_DEFAULT);
		wall = loadImage("../res/blocks/redBrick.png").getScaledInstance(Level.TILESIZE, Level.TILESIZE,
				BufferedImage.SCALE_DEFAULT);
		boxOn = loadImage("../res/blocks/boxOn.png").getScaledInstance(Level.TILESIZE, Level.TILESIZE,
				BufferedImage.SCALE_DEFAULT);
		boxOff = loadImage("../res/blocks/boxOff.png").getScaledInstance(Level.TILESIZE, Level.TILESIZE,
				BufferedImage.SCALE_DEFAULT);
		spot = loadImage("../res/blocks/spot.png").getScaledInstance(Level.TILESIZE, Level.TILESIZE,
				BufferedImage.SCALE_DEFAULT);
		outline = loadImage("../res/blocks/outline.png").getScaledInstance(64, 64, BufferedImage.SCALE_DEFAULT);
		outline2 = loadImage("../res/blocks/outline2.png").getScaledInstance(64, 64, BufferedImage.SCALE_DEFAULT);

		font48 = loadFont("src\\res\\fonts\\square.ttf", 48);
		font22 = loadFont("src\\\\res\\\\fonts\\\\square.ttf", 22);
		font30 = loadFont("src\\\\res\\\\fonts\\\\square.ttf", 30);

		// font22 = loadFont("fonts/square.ttf", 22);
		// font30 = loadFont("fonts/square.ttf", 30);

	}

	public static BufferedImage loadImage(String path) {
		try {
			return ImageIO.read(Assets.class.getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Font loadFont(String path, int size) {
		try {
			return Font.createFont(Font.TRUETYPE_FONT, new File(path)).deriveFont(Font.PLAIN, size);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
