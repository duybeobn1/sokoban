package modele.State;

import java.awt.Graphics;

import modele.Jeu;
import modele.Level;

public class GameState extends State{
	
	private Level level;
	
	public GameState(Jeu jeu) {
		super(jeu);
	}
	
	@Override
	public void update() {
		level.update();
	}

	@Override
	public void render(Graphics g) {
		level.render(g);
	}
	
	public void setLevel(Level level){
		this.level = level;
	}
	
}
